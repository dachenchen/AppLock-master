package com.chenandroid;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;
import android.widget.TextView;


public class Main2Activity extends AppCompatActivity  implements TabHost.OnTabChangeListener {
    public FragmentTabHost mTabHost;
    private Class[] mFragments = new Class[] { UnLockFragment.class,
            LockFragment.class};
    private int[] mTabSelectors = new int[] { R.drawable.btn_home_selector,
            R.drawable.btn_unlock_selector
           };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        AppManager.getInstance().addActivty(this);

         mTabHost = (FragmentTabHost) findViewById(R.id.tabhost);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        addTab();




    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.change_theme) {

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.e("cc", "onKeyDown: event.getKeyCode()"+event.getKeyCode() );
        if (KeyEvent.KEYCODE_BACK == event.getKeyCode()) {
            AppManager.getInstance().removeAndFinishActivity(Main2Activity.this);
        }
        if (KeyEvent.KEYCODE_HOME  == event.getKeyCode()) {
            Log.e("aa", "onKeyDown:   home" );
            AppManager.getInstance().removeAndFinishActivity(Main2Activity.this);
        }
        return super.onKeyDown(keyCode, event);
    }
     public void addTab(){
         String[] stringArray = {"未加锁" , "已加锁"

         };
         for (int i = 0; i < stringArray.length; i++) {
             View tabIndicator = getLayoutInflater().inflate(
                     R.layout.tab_indicator, null);

             TextView textview = (TextView) tabIndicator
                     .findViewById(R.id.textview);
             Drawable drawable = this.getResources().getDrawable(
                     mTabSelectors[i]);
             textview.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);   // 设置图片
             textview.setText(stringArray[i]);
             TabHost.TabSpec tabSpec = mTabHost.newTabSpec(stringArray[i]).setIndicator(tabIndicator);

             tabSpec.setIndicator(tabIndicator);

             mTabHost.addTab(tabSpec, mFragments[i], null);

         }
         mTabHost.setOnTabChangedListener(this);// 监听底下切换

         Intent intent = new Intent("android.intent.action.MAIN_BROADCAST");

         intent.putExtra("status", "true");
         sendBroadcast(intent);
     }



    @Override
    public void onTabChanged(String tabId) {
        final int size = mTabHost.getTabWidget().getTabCount();
        for (int i = 0; i < size; i++) {
            View v = mTabHost.getTabWidget().getChildAt(i);
            if (i == mTabHost.getCurrentTab()) {
                v.setSelected(true);
            } else {
                v.setSelected(false);
            }
        }

    }

}
