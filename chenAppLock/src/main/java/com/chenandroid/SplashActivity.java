package com.chenandroid;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;

import com.chenandroid.cache.ACache;

/**
 * Created by App-Android on 2016/11/7.
 */
public class SplashActivity  extends AppCompatActivity {

    private ImageView splash_iv;
    private Handler handler = new Handler(){};
    private ACache aCache;
    private int screenHeight;

    /**
     *  起始页面, 判断是否输入过密码
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        splash_iv = (ImageView) findViewById(R.id.splash_iv);
        // 初始化手势密码
        aCache = ACache.get(this);
        AppManager.getInstance().addActivty(this);
        // 获取屏幕上的高
//        screenHeight = getWindowManager().getDefaultDisplay().getHeight();
        doJump();
    }
    private void doJump() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String gesturePassword = aCache.getAsString(Constant.GESTURE_PASSWORD);
                if(gesturePassword == null || "".equals(gesturePassword)) {
                    //
                    Intent intent = new Intent(SplashActivity.this, CreateGestureActivity.class);
                    startActivity(intent);
                    AppManager.getInstance().removeAndFinishActivity(SplashActivity.this);
                } else {
                    Intent intent = new Intent(SplashActivity.this, GestureLoginActivity.class);
                    startActivity(intent);
                    AppManager.getInstance().removeAndFinishActivity(SplashActivity.this);
                }
            }
        }, 2000);
    }


}
