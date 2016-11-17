package com.chenandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.chenandroid.toolbar.ToolBarActivity;

/**
 * 密码保护中心
 * Created by App-Android on 2016/11/7.
 */
public class PSDBAohu extends ToolBarActivity {


    private EditText phone;


    @Override
    protected void intView() {
         title.setText("密码保护中心");
        phone = (EditText) findViewById(R.id.ed_myemail);
        findViewById(R.id.bt_finish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ;
                String  stringphone=    phone.getText().toString();
                if (TextUtils.isEmpty(stringphone)){
                    Toast.makeText(PSDBAohu.this,"错误",0).show();
                }  else{

                    SharedUtils.putString("stringphone",stringphone);
                    // 密码输入正确 进入设置密码
                    startActivity( new Intent( PSDBAohu.this,Main2Activity.class));
                    PSDBAohu.this.finish()   ;

                }
            }
        });
    }

    @Override
    public void onClick(View v) {
         switch ( v.getId()){
             case  R.id.reback:
                 finish();
                 break;
         }
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_perfecet;
    }
}
