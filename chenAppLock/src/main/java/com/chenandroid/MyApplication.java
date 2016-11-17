package com.chenandroid;

import android.app.Application;

import com.orm.SugarContext;
import com.tendcloud.tenddata.TCAgent;

import java.util.ArrayList;

/**
 * Created by MOON on 3/2/2016.
 */
public class MyApplication extends Application {
    public static MyApplication instance;
    public static MyApplication getInstance(){return instance;}
    private static ArrayList<String> lockList;
    public static ArrayList<String> getLockList(){return lockList;}



    public MyApplication(){

    }

    @Override
    public void onCreate() {
        super.onCreate();
        SugarContext.init(this);
        this.instance = this;
        this.lockList = new ArrayList<>();

        TCAgent.init(this, "1A57286E53114DE4AA5713D80972C016","default");
        TCAgent.setReportUncaughtExceptions(true);
    }
}
