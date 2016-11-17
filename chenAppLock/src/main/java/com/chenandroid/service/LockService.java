package com.chenandroid.service;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.Service;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.chenandroid.App;
import com.chenandroid.AppLoginActivity;
import com.chenandroid.SharedUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by MOON on 1/19/2016.
 */
public class LockService extends Service{

    private final String TAG = "LockService";
    private Handler mHandler = null;
    private final static int LOOPHANDLER = 0;
    private HandlerThread handlerThread = null;
        //保存加锁程序 的名字


    //设定检测时间的间隔，间隔太长可能造成已进入软件还未加锁的情况，
    //间隔时间太短则会加大CPU的负荷，程序更耗电。内存占用更大
    private static long cycleTime = 90;
    private List<App> apps;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        handlerThread = new HandlerThread("count_thread");
        handlerThread.start();
        apps = App.listAll(App.class);
        Notification notification = new Notification();
        notification.flags = Notification.FLAG_ONGOING_EVENT;
        notification.flags |= Notification.FLAG_NO_CLEAR;
        notification.flags |= Notification.FLAG_FOREGROUND_SERVICE;
              startForeground(1, notification);
        if (apps==null){
            return Service.START_STICKY;
        }else{
            //开始循环检查
            mHandler = new Handler(handlerThread.getLooper()) {
                public void dispatchMessage(android.os.Message msg) {
                    switch (msg.what) {
                        case LOOPHANDLER:
                            //Log.i(TAG,"do something..."+(System.currentTimeMillis()/1000));
                            /**
                             * 这里需要注意的是：isLockName是用来判断当前的topActivity是不是我们需要加锁的应用
                             * 同时还是需要做一个判断，就是是否已经对这个app加过锁了，不然会出现一个问题
                             * 当我们打开app时，启动我们的加锁界面，解锁之后，回到了app,但是这时候又发现栈顶app是
                             * 需要加锁的app,那么这时候又启动了我们加锁界面，这样就出现死循环了。
                             * 可以自行的实验一下
                             * 所以这里用isUnLockActivity变量来做判断的
                            */
                            try {
                                boolean isInputPWD = SharedUtils.getBoolean("isInputPWD", false);
                                if(isLockName()&&!isInputPWD){

                                        Log.e(TAG, "locking...");
                                        //调用了解锁界面之后，需要设置一下isUnLockActivity的值

                                        Intent intent = new Intent(LockService.this,AppLoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        //intent.putExtra("isUnLockActivity",false);
                                        startActivity(intent);

                                        //isUnLockActivity = true;
                                    }


                            } catch (PackageManager.NameNotFoundException e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                    mHandler.sendEmptyMessageDelayed(LOOPHANDLER, cycleTime);
                }
            };
            mHandler.sendEmptyMessage(LOOPHANDLER);
            //return Service.START_STICKY;

            return Service.START_STICKY;
        }

    }

    private boolean isLockName() throws PackageManager.NameNotFoundException {
        // TODO Auto-generated method stub
        List<PackageInfo> packages = getPackageManager()
                .getInstalledPackages(0);
        ActivityManager mActivityManager;
        mActivityManager = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);

        String packageName ;
        if (Build.VERSION.SDK_INT > 20) {
            UsageStatsManager usageStatsManager = (UsageStatsManager) getApplicationContext()
                    .getSystemService("usagestats");

            long ts = System.currentTimeMillis();
            List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST,0, ts);

            UsageStats recentStats = null;
            for (UsageStats usageStats : queryUsageStats) {
                if (recentStats == null || recentStats.getLastTimeUsed() < usageStats.getLastTimeUsed()) {
                    recentStats = usageStats;
                }
            }
            packageName = recentStats != null ? recentStats.getPackageName() : "";
            Log.e(TAG, "isLockName: packageName"+packageName );
        } else{
            // 5.0之前
            // 获取正在运行的任务栈(一个应用程序占用一个任务栈) 最近使用的任务栈会在最前面
            // 1表示给集合设置的最大容量 List<RunningTaskInfo> infos = am.getRunningTasks(1);
            // 获取最近运行的任务栈中的栈顶Activity(即用户当前操作的activity)的包名
            packageName = mActivityManager.getRunningTasks(1).get(0).topActivity.getPackageName();
            //Log.i(TAG,packageName);
        }


        Context context = getApplicationContext();
        PowerManager
                pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);



//        boolean isScreenOn = pm.isScreenOn();//如果为true，则表示屏幕“亮”了，否则屏幕“暗”了。
        Log.e(TAG, "isLockName:  getHomes("+getHomes() );
        if(getHomes().contains(packageName)){

                     SharedUtils.putBoolean("isInputPWD",false);

        }

//         if (lockName!=null&& packageName!=null){
//              if (lockName.size()>1){
//             Log.e(TAG, "isLockName: packageName"+packageName );
//             Log.e(TAG, "isLockName: packageName"+packageName );
              for ( App  str: apps) {


                  if (str.getPackageName().equals(packageName)) {
                      Log.e(TAG, "isLockName: jinlaile");
                      return true;
                  }
              }
//             }
//         }

        return false;
    }

    private List<String> getHomes() {
        // TODO Auto-generated method stub
        List<String> names = new ArrayList<String>();
        PackageManager packageManager = this.getPackageManager();
        //属性
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        List<ResolveInfo> resolveInfo = packageManager.queryIntentActivities(intent,
                PackageManager.MATCH_DEFAULT_ONLY);
        for(ResolveInfo ri : resolveInfo){
            names.add(ri.activityInfo.packageName);
//            System.out.println(ri.activityInfo.packageName);
        }
        return names;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
