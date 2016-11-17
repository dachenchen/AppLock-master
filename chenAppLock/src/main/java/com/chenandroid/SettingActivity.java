package com.chenandroid;

import com.chenandroid.toolbar.ToolBarActivity;

/**
 * Created by App-Android on 2016/11/10.
 */
public class SettingActivity extends ToolBarActivity  {


    @Override
    protected void intView() {
        title.setText("设置中心");

    }

    @Override
    protected int getLayout() {
        return R.layout.activity_setting;
    }
}
