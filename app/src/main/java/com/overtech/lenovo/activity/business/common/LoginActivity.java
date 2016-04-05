package com.overtech.lenovo.activity.business.common;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.overtech.lenovo.R;
import com.overtech.lenovo.activity.base.BaseActivity;
import com.overtech.lenovo.activity.MainActivity;
import com.overtech.lenovo.activity.business.common.register.RegisterUserAgreementActivity;
import com.overtech.lenovo.activity.business.controller.GetSmsCodeAndValicateActivity;
import com.overtech.lenovo.config.Projects;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private Button mDoLogin;
    private TextView mDoLostPassword;
    private TextView mDoRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById();
        initEvent();
    }

   private void findViewById(){
       mDoLogin=(Button)findViewById(R.id.btn_login);
       mDoRegister=(TextView)findViewById(R.id.tv_register_account);
       mDoLostPassword=(TextView)findViewById(R.id.tv_lost_password);
   }

    protected void initEvent() {
        mDoLogin.setOnClickListener(this);
        mDoLostPassword.setOnClickListener(this);
        mDoRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent =new Intent();
        switch (v.getId()){
            case R.id.btn_login:
                intent.setClass(this, MainActivity.class);
//                intent.setClass(this, TestActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_lost_password:
                intent.setClass(this, GetSmsCodeAndValicateActivity.class);
                intent.putExtra("flag", Projects.LOST_PASSWORD);
                startActivity(intent);
                break;
            case R.id.tv_register_account:
                intent.setClass(this,RegisterUserAgreementActivity.class);
                intent.putExtra("flag", Projects.REGISTER);
                startActivity(intent);
                break;
            default:
                break;
        }

    }
}

