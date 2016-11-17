package com.chenandroid;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.usage.UsageStatsManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.chenandroid.adapter.ListViewAdapter;
import com.chenandroid.sortlist.PinyinunComparator;
import com.chenandroid.sortlist.SideBar;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by App-Android on 2016/11/3.
 */
public class UnLockFragment  extends Fragment{

    private ListView listview;
    private ListViewAdapter listViewAdapter;


    private PinyinunComparator pinyinComparator;
    private SideBar sideBar;
    private TextView dialog;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragmennt_unlock, null);
        return inflate;
    }

     private Handler  handle= new Handler(){
         @Override
         public void handleMessage(Message msg) {
             super.handleMessage(msg);
              switch (msg.what){
                  case 1:
                      List<UnLockApp> unLockApps = UnLockApp.listAll(UnLockApp.class);

                      Collections.sort(unLockApps, pinyinComparator);
                        //  删除相同元素
                      if(!unLockApps.isEmpty()){
                          for(int i=0;i<unLockApps.size();i++){
                              for(int j=i+1;j<unLockApps.size();j++){
                                  if(unLockApps.get(i).getPackageName().equals(unLockApps.get(j).getPackageName())){
                                      unLockApps.remove(j);
                                      unLockApps.get(j).delete();
                                  }
                              }
                          }
                      }

                      listViewAdapter = new ListViewAdapter(getActivity());
                      listViewAdapter.setDate(unLockApps);
                      listview.setAdapter(listViewAdapter);

                      break;
              }
         }
     };

    @Override
    public void onResume() {
        super.onResume();
        addDate();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        sideBar = (SideBar) view.findViewById(R.id.sidrbar);
        dialog = (TextView) view.findViewById(R.id.dialog);

        sideBar.setTextView(dialog);
        listview = (ListView) view.findViewById(R.id.listview);
        // 设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @SuppressLint("NewApi")
            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                int position = listViewAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    listview.setSelection(position);
                }
            }
        });
        pinyinComparator = new PinyinunComparator();


        // 获取所有加锁的 程序


    }


    private void addDate() {
        final List<App> apps = App.listAll(App.class);
        boolean isShow = SharedUtils.getBoolean("isShow", true);

        // 获取所以的程序
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        if (currentVersion > 21) {
            if (!isNoSwitch()) {
                RequestPromission();

            } else {

                /**
                 *    是否重新刷新数据
                 */

                if ( isShow ){
                    new  Thread(new Runnable() {
                        @Override
                        public void run() {
                            showList(apps);
                        }
                    }).start();
                }
                handle.sendEmptyMessage(1);
            }
        } else {

            if ( isShow ){

                new  Thread(new Runnable() {
                    @Override
                    public void run() {
                        showList(apps);
                    }
                }).start();

            }else{
                handle.sendEmptyMessage(1);
            }
        }
    }


    public void showList(List<App> lockedApps) {

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog = new ProgressDialog(getActivity());
                progressDialog.setMessage("扫描中...");
                progressDialog.show();
            }
        });



        List<PackageInfo> packages =  getActivity(). getPackageManager()
                .getInstalledPackages(0);
        for (int i = 0; i < packages.size(); i++) {
            PackageInfo packageInfo = packages.get(i);
            if (isUserApp(packageInfo)) {



                if (!isLocked(packageInfo, lockedApps)) {
                    UnLockApp  un=  new UnLockApp();


                    un.setName( packageInfo.applicationInfo.loadLabel(
                            getActivity().    getPackageManager()).toString());
                    un.setPackageName(packageInfo.applicationInfo.packageName);
                    Drawable drawable = packageInfo.applicationInfo
                            .loadIcon(  getActivity().getPackageManager());
                    BitmapDrawable bd = (BitmapDrawable) drawable;

                    Bitmap bitmap = bd.getBitmap();

                    try{
                        boolean b = saveMyBitmap(bitmap, un.getName());
                        if (b){
                            un.setIcon(FILE_SAVEPATH + un.getName() + ".png");
                        }
                    }catch (Exception e){

                    }

                    String pinyin = Pinyin4jUtil.converterToFirstSpell(packageInfo.applicationInfo.loadLabel(
                            getActivity(). getPackageManager()).toString());
                    String sortString = pinyin.substring(0, 1).toUpperCase();
                    // 正则表达式，判断首字母是否是英文字母
                    if (sortString.matches("[A-Z]")) {
                        un.setSortLetters(sortString.toUpperCase());
                    } else {
                        un.setSortLetters("#");
                    }
                    SharedUtils.putBoolean("isShow", false);
                    un.save();

                }


            }

        }
        progressDialog.dismiss();
        handle.sendEmptyMessage(1);
        Intent intent = new Intent("android.intent.action.MAIN_BROADCAST");
        intent.putExtra("status", "false");
         getActivity().sendBroadcast(intent);

    }
    private final static String FILE_SAVEPATH = Environment
            .getExternalStorageDirectory().getAbsolutePath()
            + "/chen/";
    public boolean saveMyBitmap(Bitmap bmp, String bitName) {
        File dirFile = new File(FILE_SAVEPATH);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
        boolean flag;
        File f = new File(FILE_SAVEPATH, bitName + ".png");
        FileOutputStream out = null;
        flag=false;
        try {
            out = new FileOutputStream(f);


            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            flag=true;
        }  catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     *  程序已加锁
     * @param pInfo
     * @param lockedApps
     * @return
     */
    public boolean isLocked(PackageInfo pInfo, List<App> lockedApps) {
        for (App a : lockedApps) {
            if (pInfo.applicationInfo.packageName.equals(a.getPackageName())) {
                return true;
            }
        }
        return false;
    }
    public boolean isSystemApp(PackageInfo pInfo) {
        return ((pInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0);
    }

    public boolean isSystemUpdateApp(PackageInfo pInfo) {
        return ((pInfo.applicationInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0);
    }

    public boolean isUserApp(PackageInfo pInfo) {
        return (!isSystemApp(pInfo) && !isSystemUpdateApp(pInfo));
    }
    public void RequestPromission() {
        new AlertDialog.Builder(  getActivity()).
                setTitle("设置").
                setMessage("开启usagestats权限")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
                        startActivity(intent);
                        //finish();
                    }
                }).show();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private boolean isNoSwitch() {
        long ts = System.currentTimeMillis();
        UsageStatsManager usageStatsManager = (UsageStatsManager) MyApplication.getInstance()
                .getSystemService("usagestats");
        List queryUsageStats = usageStatsManager.queryUsageStats(
                UsageStatsManager.INTERVAL_BEST, 0, ts);
        return !(queryUsageStats == null || queryUsageStats.isEmpty());
    }
}
