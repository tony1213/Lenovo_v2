package com.overtech.lenovo.activity.business.common;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

<<<<<<< HEAD
import com.google.gson.Gson;
=======
>>>>>>> origin/master
import com.overtech.lenovo.R;
import com.overtech.lenovo.activity.MainActivity;
import com.overtech.lenovo.activity.base.BaseActivity;
import com.overtech.lenovo.activity.business.common.register.RegisterUserAgreementActivity;
import com.overtech.lenovo.activity.business.controller.GetSmsCodeAndValicateActivity;
import com.overtech.lenovo.config.Projects;
import com.overtech.lenovo.debug.Logger;
import com.overtech.lenovo.entity.common.Employee;
import com.overtech.lenovo.entity.common.Test;
import com.overtech.lenovo.http.HttpEngine;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private Button mDoLogin;
    private TextView mDoLostPassword;
    private TextView mDoRegister;

    @Override
    protected int getLayoutIds() {
        return R.layout.activity_login;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        mDoLogin = (Button) findViewById(R.id.btn_login);
        mDoRegister = (TextView) findViewById(R.id.tv_register_account);
        mDoLostPassword = (TextView) findViewById(R.id.tv_lost_password);

        mDoLogin.setOnClickListener(this);
        mDoLostPassword.setOnClickListener(this);
        mDoRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btn_login:
               //intent.setClass(this, TestActivity.class);
                doLogin();
//                intent.setClass(this, MainActivity.class);
//                startActivity(intent);
                break;
            case R.id.tv_lost_password:
//                intent.setClass(this, GetSmsCodeAndValicateActivity.class);
//                intent.putExtra("flag", Projects.LOST_PASSWORD);
//                startActivity(intent);
                doInvidateLogin();
                break;
            case R.id.tv_register_account:
<<<<<<< HEAD
//                intent.setClass(this,RegisterUserAgreementActivity.class);
//                intent.putExtra("flag", Projects.REGISTER);
//                startActivity(intent);
                doLogout();
=======
                intent.setClass(this, RegisterUserAgreementActivity.class);
                intent.putExtra("flag", Projects.REGISTER);
                startActivity(intent);
>>>>>>> origin/master
                break;
            default:
                break;
        }

    }

    private void doLogin(){
        String url="http://192.168.1.123:8080/test/MobileServlet";
        Test test=new Test();
        test.setDesc("Login");
        test.setOs("android");
        test.setVer("v1.0");
        test.setLanguage("zh");
        test.setCmd("1");
        test.setUid("admin");
        test.setPwd("12345");
        test.setTenantcode("test");
        Gson gson=new Gson();
        String json=gson.toJson(test);
        Request request=httpEngine.createRequest(url, json);
        Call call=httpEngine.createRequestCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Logger.e(request.toString());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Logger.e("header:"+response.headers().get("Set-Cookie")+",body:"+response.body().string());
            }
        });
    }
    private void doInvidateLogin(){
        String url="http://192.168.1.123:8080/test/MobileServlet";
        Test test=new Test();
        test.setDesc("Get User List");
        test.setOs("android");
        test.setVer("v1.0");
        test.setLanguage("zh");
        test.setCmd("10000");
        test.setUid("2");
        test.setPwd("");
        test.setTenantcode("test");
        Gson gson=new Gson();
        String json=gson.toJson(test);
        Request request=httpEngine.createRequest(url, json);
        Call call=httpEngine.createRequestCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Logger.e(request.toString());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Logger.e("header:"+response.headers().get("Set-Cookie")+",body:"+response.body().string());
            }
        });
    }
    private void doLogout(){
        String url="http://192.168.1.123:8080/test/MobileServlet";
        Test test=new Test();
        test.setDesc("Logout");
        test.setOs("android");
        test.setVer("v1.0");
        test.setLanguage("zh");
        test.setCmd("2");
        test.setUid("2");
        test.setPwd("");
        test.setTenantcode("test");
        Gson gson=new Gson();
        String json=gson.toJson(test);
        Request request=httpEngine.createRequest(url, json);
        Call call=httpEngine.createRequestCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Logger.e(request.toString());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Logger.e("header:"+response.headers().get("Set-Cookie")+",body:"+response.body().string());
            }
        });
    }

}

