package com.chenandroid.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import com.chenandroid.App;
import com.chenandroid.Pinyin4jUtil;
import com.chenandroid.UnLockApp;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 *   程序 的安装 和卸载  广播
 * Created by App-Android on 2016/11/7.
 */
public class BootReceiver extends BroadcastReceiver {
    private final static String FILE_SAVEPATH = Environment
            .getExternalStorageDirectory().getAbsolutePath()
            + "/chen";
    @Override
    public void onReceive(Context context, Intent intent) {
        //接收广播：系统启动完成后运行程序
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
        }
        //接收广播：设备上新安装了一个应用程序包后自动启动新安装应用程序。
        if (intent.getAction().equals("android.intent.action.PACKAGE_ADDED")) {
            /**
             *   添加一个程序,  添加到 原来数据库中

             */
            PackageManager packageManager=context.getPackageManager();
            String packageName = intent.getDataString().substring(8);
            try {
                PackageInfo packageInfo=packageManager.getPackageInfo(packageName, 0);
                ApplicationInfo applicationInfo = packageInfo.applicationInfo;
                UnLockApp  un= new UnLockApp();
                un.setName(    applicationInfo.name);
                un.setPackageName(applicationInfo.packageName);



                Drawable drawable = packageInfo.applicationInfo
                        .loadIcon(context.getPackageManager());
                BitmapDrawable bd = (BitmapDrawable) drawable;

                Bitmap bitmap = bd.getBitmap();
                if (saveMyBitmap(bitmap,un.getName()))   {
                    un.setIcon(FILE_SAVEPATH+un.getName()+".png");
                }
                String pinyin = Pinyin4jUtil.converterToFirstSpell(packageInfo.applicationInfo.loadLabel(
                       context.getPackageManager()).toString());
                String sortString = pinyin.substring(0, 1).toUpperCase();
                // 正则表达式，判断首字母是否是英文字母
                if (sortString.matches("[A-Z]")) {
                    un.setSortLetters(sortString.toUpperCase());
                } else {
                    un.setSortLetters("#");
                }
                un.save();
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }


        }
        //接收广播：设备上删除了一个应用程序包。
        if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
            String packageName = intent.getDataString().substring(8);
            List<App> apps = App.find(App.class, "package_name=?",packageName);
                   for ( App  a: apps){
                        if (a.getPackageName().equals(packageName)){
                            a.delete();
                        }
                   }



        }


    }

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
}
