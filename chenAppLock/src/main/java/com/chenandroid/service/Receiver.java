package com.chenandroid.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 *  用广播 来 扫描后台开启的服务
 * Created by MOON on 1/23/2016.
 */
public class Receiver extends BroadcastReceiver {
    private static final String TAG =Receiver.class.getSimpleName() ;
    static private Context mainContext ;
    private Intent servIntent;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (mainContext == null){
            this.mainContext = context;
        }

        if (intent.getAction().equals("android.intent.action.SET_BROADCAST")){
                Intent servIntent = new Intent(context, LockService.class);
                mainContext.startService(servIntent);
            return;
        }
         if ( intent.getAction().equals(Intent.ACTION_USER_PRESENT)){
             Log.e(TAG, "onReceive: 屏幕亮了" );
             servIntent = new Intent(context, LockService.class);
             mainContext.startService(servIntent);

         }
         if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)){
             Log.e(TAG, "onReceive: 屏幕了" );
             mainContext.stopService(servIntent);
         }
        if (intent.getAction().equals("android.intent.action.MAIN_BROADCAST")){

                Intent servIntent = new Intent(context, LockService.class);
                mainContext.startService(servIntent);
                return;

        }
    }
}
