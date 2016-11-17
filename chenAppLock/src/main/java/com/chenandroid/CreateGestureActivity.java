package com.chenandroid;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chenandroid.cache.ACache;
import com.chenandroid.toolbar.ToolBarActivity;
import com.star.lockpattern.util.LockPatternUtil;
import com.star.lockpattern.widget.LockPatternIndicator;
import com.star.lockpattern.widget.LockPatternView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by App-Android on 2016/11/7.
 */
public class CreateGestureActivity extends ToolBarActivity implements View.OnClickListener{
    LockPatternView lockPatternView;
    // 手势密码
    private List<LockPatternView.Cell> mChosenPattern = null;
    private TextView messageTv;
    private LockPatternIndicator lockPatternIndicator;
    private ACache aCache;
    private Button resetBtn;
    private int key;


    @Override
    protected void intView() {
         title.setText("设置密码");
        aCache = ACache.get(CreateGestureActivity.this);
        key = getIntent().getIntExtra("key", 0);
        lockPatternView  = (LockPatternView) findViewById(R.id.lockPatternView);
        lockPatternIndicator = (LockPatternIndicator) findViewById(R.id.lockPatterIndicator);
        resetBtn = (Button) findViewById(R.id.resetBtn);
        resetBtn.setOnClickListener(this);
        messageTv = (TextView) findViewById(R.id.messageTv);
        // 设置手势密码
        lockPatternView.setOnPatternListener(new LockPatternView.OnPatternListener() {
            @Override
            public void onPatternStart() {
                //  清空原来的手势密码
                lockPatternView.removePostClearPatternRunnable();
                //updateStatus(Status.DEFAULT, null);
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
            }

            @Override
            public void onPatternComplete(List<LockPatternView.Cell> pattern) {
                if(mChosenPattern == null && pattern.size() >= 4) {
                    mChosenPattern = new ArrayList<LockPatternView.Cell>(pattern);
                    updateStatus(Status.CORRECT, pattern);
                } else if (mChosenPattern == null && pattern.size() < 4) {
                    updateStatus(Status.LESSERROR, pattern);
                } else if (mChosenPattern != null) {
                    if (mChosenPattern.equals(pattern)) {
                        updateStatus(Status.CONFIRMCORRECT, pattern);
                    } else {
                        updateStatus(Status.CONFIRMERROR, pattern);
                    }
                }
            }
        });
    }

    private static final long DELAYTIME = 600L;

    /**
     * 更新状态
     * @param status
     * @param pattern
     */
    private void updateStatus(Status status, List<LockPatternView.Cell> pattern) {
        messageTv.setTextColor(getResources().getColor(status.colorId));
        messageTv.setText(status.strId);
        switch (status) {
            case DEFAULT:
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                break;
            case CORRECT:
                updateLockPatternIndicator();
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                break;
            case LESSERROR:
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                break;
            case CONFIRMERROR:
                lockPatternView.setPattern(LockPatternView.DisplayMode.ERROR);
                lockPatternView.postClearPatternRunnable(DELAYTIME);
                break;
            case CONFIRMCORRECT:
                saveChosenPattern(pattern);
                lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                 if (key==1){
                     Intent intent = new Intent(this, Main2Activity.class);
                     intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                     startActivity( intent );
                     CreateGestureActivity.this. finish();

                 } else {
                     setLockPatternSuccess();
                 }

                break;
        }
    }
    /**
    * 更新 Indicator
    */
    private void updateLockPatternIndicator() {
        if (mChosenPattern == null)
            return;
        lockPatternIndicator.setIndicator(mChosenPattern);
    }

    /**
     * 成功设置了手势密码(跳到首页)
     */
    private void setLockPatternSuccess() {
        Intent intent = new Intent(this, PSDBAohu.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity( intent );


        CreateGestureActivity.this.   finish();
    }

    /**
     * 保存手势密码
     */
    private void saveChosenPattern(List<LockPatternView.Cell> cells) {
        byte[] bytes = LockPatternUtil.patternToHash(cells);
        aCache.put(Constant.GESTURE_PASSWORD, bytes);
    }

    @Override
    public void onClick(View v) {
            switch (v.getId()){
                // 重新设置手势密码
                case R.id.resetBtn:
                    mChosenPattern = null;
                    lockPatternIndicator.setDefaultIndicator();
                    updateStatus(Status.DEFAULT, null);
                    lockPatternView.setPattern(LockPatternView.DisplayMode.DEFAULT);
                     break;
                case  R.id.reback:
                    finish();
                    break;
            }
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_crate;
    }

    /**
     *  手势密码状态
     */
    private enum Status {
        //默认的状态，刚开始的时候（初始化状态）
        DEFAULT(R.string.create_gesture_default, R.color.grey_a5a5a5),
        //第一次记录成功
        CORRECT(R.string.create_gesture_correct, R.color.grey_a5a5a5),
        //连接的点数小于4（二次确认的时候就不再提示连接的点数小于4，而是提示确认错误）
        LESSERROR(R.string.create_gesture_less_error, R.color.red_f4333c),
        //二次确认错误
        CONFIRMERROR(R.string.create_gesture_confirm_error, R.color.red_f4333c),
        //二次确认正确
        CONFIRMCORRECT(R.string.create_gesture_confirm_correct, R.color.grey_a5a5a5);

        private Status(int strId, int colorId) {
            this.strId = strId;
            this.colorId = colorId;
        }
        private int strId;
        private int colorId;
    }
}
