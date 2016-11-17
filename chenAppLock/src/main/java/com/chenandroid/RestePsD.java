package com.chenandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.chenandroid.toolbar.ToolBarActivity;

/**
 *  忘记密保回到问题
 * Created by App-Android on 2016/11/7.
 */
public class RestePsD extends ToolBarActivity implements View.OnClickListener{

    private EditText ed_phone;



    @Override
    protected void intView() {
         title.setText("找回密码");
        ed_phone = (EditText) findViewById(R.id.ed_phone);

        findViewById(R.id.bt_finish).setOnClickListener( this);
    }

    @Override
    public void onClick(View v) {
            switch (v.getId()){
                case  R.id.bt_finish:
                    String stringed_ed_phone = ed_phone.getText().toString();
                    String stringphone = SharedUtils.getString("stringphone");
                     if (!stringed_ed_phone.equals(stringphone)){
                         Toast.makeText(RestePsD.this,"输入的手机号码错误 ",0).show();
                     }else{

                         Intent intent = new Intent(RestePsD.this, CreateGestureActivity.class);
                         intent.putExtra("key",1);
                         startActivity( intent);
                     }
                     break;

                case  R.id.reback:
                     finish();
                     break;
            }
    }

    @Override
    protected int getLayout() {
        return R.layout.resterpsd;
    }
}
