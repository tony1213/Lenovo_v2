package com.overtech.lenovo.activity.business.common;


import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.overtech.lenovo.R;
import com.overtech.lenovo.activity.MainActivity;
import com.overtech.lenovo.activity.base.BaseActivity;
import com.overtech.lenovo.activity.business.common.password.FindbackPasswordActivity;
import com.overtech.lenovo.activity.business.common.register.RegisterActivity;
import com.overtech.lenovo.config.StatusCode;
import com.overtech.lenovo.config.SystemConfig;
import com.overtech.lenovo.debug.Logger;
import com.overtech.lenovo.entity.RequestExceptBean;
import com.overtech.lenovo.entity.Requester;
import com.overtech.lenovo.entity.ResponseExceptBean;
import com.overtech.lenovo.entity.common.Common;
import com.overtech.lenovo.http.webservice.UIHandler;
import com.overtech.lenovo.utils.SharePreferencesUtils;
import com.overtech.lenovo.utils.SharedPreferencesKeys;
import com.overtech.lenovo.utils.Utilities;
import com.overtech.lenovo.widget.EditTextWithDelete;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private Button mDoLogin;
    private TextView mDoLostPassword;
    private TextView mDoRegister;
    private ToggleButton toggleButton;
    private EditTextWithDelete etLoginName;
    private EditTextWithDelete etLoginPwd;
    private UIHandler uiHandler = new UIHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            String json = (String) msg.obj;
            Logger.e("登陆传过来的信息===" + json);
            Common bean = gson.fromJson(json, Common.class);
            if (bean == null) {
                Utilities.showToast("登录失败", LoginActivity.this);
                stopProgress();
                return;
            }
            int st = bean.st;
            if (st != 0) {
                Utilities.showToast(bean.msg, LoginActivity.this);
                stopProgress();
                return;
            }

            switch (msg.what) {
                case StatusCode.FAILED:
                    Utilities.showToast(bean.msg, LoginActivity.this);
                    break;
                case StatusCode.SERVER_EXCEPTION:
                    Utilities.showToast(bean.msg, LoginActivity.this);
                    break;
                case StatusCode.LOGIN_SUCCESS:
                    SharePreferencesUtils.put(LoginActivity.this, SharedPreferencesKeys.UID, bean.body.uid + "");
                    Intent intent = new Intent();
                    intent.setClass(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
            stopProgress();
        }
    };

    @Override
    protected int getLayoutIds() {
        return R.layout.activity_login;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        mDoLogin = (Button) findViewById(R.id.btn_login);
        mDoRegister = (TextView) findViewById(R.id.tv_register_account);
        mDoLostPassword = (TextView) findViewById(R.id.tv_lost_password);
        toggleButton = (ToggleButton) findViewById(R.id.tb_change_password);
        etLoginName = (EditTextWithDelete) findViewById(R.id.et_login_username);
        etLoginPwd = (EditTextWithDelete) findViewById(R.id.et_login_password);

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    etLoginPwd.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    etLoginPwd.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        mDoLogin.setOnClickListener(this);
        mDoLostPassword.setOnClickListener(this);
        mDoRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.btn_login:
                doLogin();
                break;
            case R.id.tv_lost_password:
                intent.setClass(this, FindbackPasswordActivity.class);
//                intent.putExtra("flag", Projects.LOST_PASSWORD);
                startActivity(intent);
                break;
            case R.id.tv_register_account:
                intent.setClass(this, RegisterActivity.class);
//                intent.putExtra("flag", Projects.REGISTER);
                startActivity(intent);
                break;
            default:
                break;
        }

    }

    private void doLogin() {
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
        startProgress("登录中...");


        Requester requester = new Requester();
        requester.cmd = 1;
        requester.uid = name;
        requester.pwd = pwd;
        Request request = httpEngine.createRequest(SystemConfig.IP, gson.toJson(requester));
        Call call = httpEngine.createRequestCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Logger.e(request.toString());
                Message msg = uiHandler.obtainMessage();
                RequestExceptBean bean = new RequestExceptBean();
                bean.st = 0;
                bean.msg = getResources().getString(R.string.common_request_exception);
                msg.what = StatusCode.FAILED;
                msg.obj = gson.toJson(bean);
                uiHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Message msg = uiHandler.obtainMessage();
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    msg.what = StatusCode.LOGIN_SUCCESS;
                    msg.obj = json;
                } else {
                    ResponseExceptBean bean = new ResponseExceptBean();
                    bean.st = response.code();
                    bean.msg = response.message();
                    msg.what = StatusCode.SERVER_EXCEPTION;
                    msg.obj = gson.toJson(bean);
                }
                uiHandler.sendMessage(msg);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}

