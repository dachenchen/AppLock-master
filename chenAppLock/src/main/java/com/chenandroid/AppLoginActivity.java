package com.chenandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chenandroid.cache.ACache;
import com.star.lockpattern.util.LockPatternUtil;
import com.star.lockpattern.widget.LockPatternIndicator;
import com.star.lockpattern.widget.LockPatternView;

import java.util.List;

/**
 * Created by App-Android on 2016/11/7.
 *  登录的手势密码
 */
public class AppLoginActivity extends AppCompatActivity  implements View.OnClickListener{

    private byte[] asBinary;
    private ACache aCache;
    private LockPatternIndicator lockPatterIndicator;
    private TextView messageTv;
    private LockPatternView lockPatternView;
    private Button forgetGestureBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        Log.e("ccc", "onCreate: " );
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gesture_login);
        lockPatterIndicator = (LockPatternIndicator) findViewById(R.id.lockPatterIndicator);
        messageTv = (TextView) findViewById(R.id.messageTv);
        lockPatternView = (LockPatternView) findViewById(R.id.lockPatternView);
        forgetGestureBtn = (Button) findViewById(R.id.forgetGestureBtn);
        forgetGestureBtn.setOnClickListener( this);
        aCache = ACache.get(AppLoginActivity.this);
        AppManager.getInstance().addActivty(this);
        asBinary = aCache.getAsBinary(Constant.GESTURE_PASSWORD);
        lockPatternView.setOnPatternListener(new LockPatternView.OnPatternListener() {
            @Override
            public void onPatternStart() {
                //  初始化删除密码
                lockPatternView.removePostClearPatternRunnable();
            }

            @Override
            public void onPatternComplete(List<LockPatternView.Cell> pattern) {
                if(pattern != null){
                    // 判断输入的密码和保存的验证码 是否一致
                    if(LockPatternUtil.checkPattern(pattern, asBinary)) {
                        updateStatus(Status.CORRECT);
                    } else {
                        updateStatus(Status.ERROR);
                    }
                }
            }
        });



    }
    private static final long DELAYTIME = 600l;
    /**
     * 更新状态
     * @param status
     */
    private void updateStatus(Status status) {
        messageTv.setText(status.strId);
        messageTv.setTextColor(getResources().getColor(status.colorId));
        switch (status) {
            case DEFAULT:
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                break;
            case ERROR:
                lockPatternView.setPattern(LockPatternView.DisplayMode.ERROR);
                lockPatternView.postClearPatternRunnable(DELAYTIME);
                break;
            case CORRECT:
             SharedUtils.putBoolean("isInputPWD",true);

                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                loginGestureSuccess();
                break;
        }
    }

    /**
     *  登录成功
     */
    private void loginGestureSuccess() {

        AppManager.getInstance().removeAndFinishActivity(this);
        AppManager.getInstance().removeAndFinishActivity(Main2Activity.class);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.resetBtn:
                startActivity( new Intent(this, RestePsD.class ));
                this.finish();
                 break;
        }
    }

    private enum Status {
        //默认的状态
        DEFAULT(R.string.gesture_default, R.color.grey_a5a5a5),
        //密码输入错误
        ERROR(R.string.gesture_error, R.color.red_f4333c),
        //密码输入正确
        CORRECT(R.string.gesture_correct, R.color.grey_a5a5a5);

        private Status(int strId, int colorId) {
            this.strId = strId;
            this.colorId = colorId;
        }
        private int strId;
        private int colorId;
    }
}
