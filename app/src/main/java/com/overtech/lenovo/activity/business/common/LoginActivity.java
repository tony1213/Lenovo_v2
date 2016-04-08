package com.overtech.lenovo.activity.business.common;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.overtech.lenovo.R;
import com.overtech.lenovo.activity.MainActivity;
import com.overtech.lenovo.activity.base.BaseActivity;
import com.overtech.lenovo.activity.business.common.register.RegisterUserAgreementActivity;
import com.overtech.lenovo.activity.business.controller.GetSmsCodeAndValicateActivity;
import com.overtech.lenovo.config.Projects;
import com.overtech.lenovo.debug.Logger;
import com.overtech.lenovo.entity.Requester;
import com.overtech.lenovo.entity.common.Employee;
import com.overtech.lenovo.utils.Utilities;
import com.overtech.lenovo.widget.EditTextWithDelete;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private Button mDoLogin;
    private TextView mDoLostPassword;
    private TextView mDoRegister;
    private EditTextWithDelete etLoginName;
    private EditTextWithDelete etLoginPwd;

    @Override
    protected int getLayoutIds() {
        return R.layout.activity_login;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        mDoLogin = (Button) findViewById(R.id.btn_login);
        mDoRegister = (TextView) findViewById(R.id.tv_register_account);
        mDoLostPassword = (TextView) findViewById(R.id.tv_lost_password);
        etLoginName = (EditTextWithDelete) findViewById(R.id.et_login_username);
        etLoginPwd = (EditTextWithDelete) findViewById(R.id.et_login_password);

        mDoLogin.setOnClickListener(this);
        mDoLostPassword.setOnClickListener(this);
        mDoRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btn_login:
//             doLogin();
                intent.setClass(this, MainActivity.class);
                startActivity(intent);
                finish();
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

    private void doLogin() {
        String url = "http://192.168.1.123:8080/test/MobileServlet";
        String name = etLoginName.getText().toString().trim();
        String pwd = etLoginPwd.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            Utilities.showToast("用户名不能为空", this);
            return;
        }
        if (TextUtils.isEmpty(pwd)) {
            Utilities.showToast("密码不能为空", this);
            return;
        }
        Employee employee = new Employee();
        employee.loginName = name;
        employee.password = pwd;

        Requester requester = new Requester();
        requester.cmd = 10001;
        requester.body.put("data", employee);
        Request request = httpEngine.createRequest(url, requester.toString());
        Logger.e(requester.toString());
//        Call call = httpEngine.createRequestCall(request);
//        call.enqueue(new Callback() {
//            @Override
//            public void onFailure(Request request, IOException e) {
//                Logger.e(request.toString());
//            }
//
//            @Override
//            public void onResponse(Response response) throws IOException {
//                Logger.e("header:" + response.headers().get("Set-Cookie") + ",body:" + response.body().string());
//            }
//        });
    }





    private void doInvidateLogin() {
        String url = "http://192.168.1.123:8080/test/MobileServlet";

        Request request = httpEngine.createRequest(url);
        Call call = httpEngine.createRequestCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Logger.e(request.toString());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Logger.e("header:" + response.headers().get("Set-Cookie") + ",body:" + response.body().string());
            }
        });
    }

    private void doLogout() {
        String url = "http://192.168.1.123:8080/test/MobileServlet";

        Request request = httpEngine.createRequest(url);
        Call call = httpEngine.createRequestCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Logger.e(request.toString());
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Logger.e("header:" + response.headers().get("Set-Cookie") + ",body:" + response.body().string());
            }
        });
    }

}

