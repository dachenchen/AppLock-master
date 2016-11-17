package com.chenandroid.toolbar;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chenandroid.AppManager;
import com.chenandroid.R;


/**
 *ToolBar 的封装 避免每个activity重写再 写toolbar
 * time : 10:26
 */
public abstract class ToolBarActivity extends AppCompatActivity implements View.OnClickListener {
    private ToolBarHelper mToolBarHelper ;
    public Toolbar toolbar ;
   public Context mContext;
    public TextView title;
    private RelativeLayout reback;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());

        AppManager. getInstance().addActivty(this);
        intView();
        intDaTa();
    }

    @Override
    public void setContentView(int layoutResID) {

        mContext=getApplicationContext();
//        MyActivityManager.getInstance().pushOneActivity(this);
        mToolBarHelper = new ToolBarHelper(this,layoutResID) ;
        toolbar = mToolBarHelper.getToolBar() ;
        setContentView(mToolBarHelper.getContentView());
        /*把 toolbar 设置到Activity 中*/
        setSupportActionBar(toolbar);
        /*自定义的一些操作*/
        reback = (RelativeLayout) toolbar.findViewById(R.id.reback);
        reback.setOnClickListener(this);
        title = (TextView) toolbar.findViewById(R.id.title);


    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {// 返回键
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            comeBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 返回到上一个界面
     */
    protected void comeBack() {
        AppManager.getInstance().removeAndFinishActivity(this);
    }








    /**
     *  处理  事件
     */
    protected  void intDaTa(){

    }
// @ Subscribe
    /**
     *   查找 控件
     */
    protected abstract void intView();

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.reback:
                AppManager.getInstance().removeAndFinishActivity(this);
                break;
        }

    }
    protected abstract int getLayout();

    @Override
    protected void onDestroy() {
        super.onDestroy();

//        EventBus.getDefault().unregister(this);
        AppManager.getInstance().removeActivity(this);

    }

}