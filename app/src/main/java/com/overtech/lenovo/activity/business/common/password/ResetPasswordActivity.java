package com.overtech.lenovo.activity.business.common.password;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.View;

import com.overtech.lenovo.R;
import com.overtech.lenovo.activity.base.BaseActivity;
import com.overtech.lenovo.activity.business.common.LoginActivity;
import com.overtech.lenovo.config.StatusCode;
import com.overtech.lenovo.config.SystemConfig;
import com.overtech.lenovo.debug.Logger;
import com.overtech.lenovo.entity.RequestExceptBean;
import com.overtech.lenovo.entity.Requester;
import com.overtech.lenovo.entity.ResponseExceptBean;
import com.overtech.lenovo.entity.common.Common;
import com.overtech.lenovo.http.webservice.UIHandler;
import com.overtech.lenovo.utils.StackManager;
import com.overtech.lenovo.utils.Utilities;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/*
*@author Will
*@description 重置密码
*/
public class ResetPasswordActivity extends BaseActivity implements View.OnClickListener {
    private AppCompatEditText etNewPassword;
    private AppCompatEditText etNewPasswordConfirm;
    private AppCompatButton btSubmitPwd;
    private String phone;
    private UIHandler uiHandler = new UIHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String json = (String) msg.obj;
            Logger.e("resetpasswordactivity===" + json);
            Common bean = gson.fromJson(json, Common.class);
            if (bean == null) {
                return;
            }
            int st = bean.st;
            switch (msg.what) {
                case StatusCode.FAILED:
                    Utilities.showToast(bean.msg, ResetPasswordActivity.this);
                    break;
                case StatusCode.SERVER_EXCEPTION:
                    Utilities.showToast(bean.msg, ResetPasswordActivity.this);
                    break;
                case StatusCode.FINDBACK_RESET_PASSWORD_SUCCESS:
                    Utilities.showToast(bean.msg, ResetPasswordActivity.this);
                    if (st == 0) {
                        Intent intent = new Intent(ResetPasswordActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                        StackManager.getStackManager().popAllActivitys();
                    }
                    break;
            }
            btSubmitPwd.setEnabled(true);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.e("resetPasswordActivity======onDestroy");
    }

    @Override
    protected int getLayoutIds() {
        return R.layout.activity_reset_password;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        phone = getIntent().getStringExtra("phone");
        Logger.e("发送过来的手机号  "+phone);
        etNewPassword = (AppCompatEditText) findViewById(R.id.et_newpassword);
        etNewPasswordConfirm = (AppCompatEditText) findViewById(R.id.et_password_confirm);
        btSubmitPwd = (AppCompatButton) findViewById(R.id.bt_reset_pwd_confirm);

        btSubmitPwd.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_reset_pwd_confirm:
                String sNewPassword = etNewPassword.getText().toString().trim();
                String sNewPasswordConfirm = etNewPasswordConfirm.getText().toString().trim();
                if (TextUtils.isEmpty(sNewPassword) || TextUtils.isEmpty(sNewPasswordConfirm)) {
                    Utilities.showToast("输入不能为空", this);
                    return;
                }
                if (sNewPassword.length() < 6 || sNewPassword.length() > 18 || sNewPasswordConfirm.length() < 6 || sNewPasswordConfirm.length() > 18) {
                    Utilities.showToast("密码长度为6-18位", this);
                    return;
                }
                if (!TextUtils.equals(sNewPassword, sNewPasswordConfirm)) {
                    Utilities.showToast("密码输入不相同", this);
                    return;
                }
                btSubmitPwd.setEnabled(false);
                submitPwd(sNewPassword);
                break;
        }
    }

    private void submitPwd(String pwd) {
        Requester requester = new Requester();
        requester.cmd = 6;
        requester.body.put("pwd", pwd);
        requester.body.put("phone", phone);
        Request request = httpEngine.createRequest(SystemConfig.IP, gson.toJson(requester));
        Call call = httpEngine.createRequestCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                Message msg = uiHandler.obtainMessage();
                RequestExceptBean bean = new RequestExceptBean();
                bean.st = 0;
                bean.msg = "网络异常";
                msg.what = StatusCode.FAILED;
                msg.obj = gson.toJson(bean);
                uiHandler.sendMessage(msg);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                Message msg = uiHandler.obtainMessage();
                if (response.isSuccessful()) {
                    String json = response.body().string();
                    msg.what = StatusCode.FINDBACK_RESET_PASSWORD_SUCCESS;
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
}
