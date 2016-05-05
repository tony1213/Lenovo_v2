package com.overtech.lenovo.activity.business.common.register;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.PopupWindowCompat;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.overtech.lenovo.R;
import com.overtech.lenovo.activity.base.BaseActivity;
import com.overtech.lenovo.config.StatusCode;
import com.overtech.lenovo.config.SystemConfig;
import com.overtech.lenovo.debug.Logger;
import com.overtech.lenovo.entity.RequestExceptBean;
import com.overtech.lenovo.entity.Requester;
import com.overtech.lenovo.entity.ResponseExceptBean;
import com.overtech.lenovo.entity.common.Common;
import com.overtech.lenovo.http.webservice.UIHandler;
import com.overtech.lenovo.utils.Utilities;
import com.overtech.lenovo.widget.TimeButton;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private AppCompatEditText etPhone;
    private AppCompatEditText etPwd;
    private AppCompatEditText etSmsCode;
    private TimeButton btGetSmsCode;
    private AppCompatCheckBox cbPrivacy;
    private AppCompatTextView tvPrivacyContetent;
    private AppCompatButton btRegisterUpload;
    private PopupWindow privacyPopup;
    private String sessionId;
    private UIHandler uiHandler = new UIHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String json = (String) msg.obj;
            Logger.e(json);
            if (json == null) {
                return;
            }
            Common bean = gson.fromJson(json, Common.class);
            int st=bean.st;
            switch (msg.what) {
                case StatusCode.FAILED:
                    Utilities.showToast(bean.msg, RegisterActivity.this);
                    break;
                case StatusCode.SERVER_EXCEPTION:
                    Utilities.showToast(bean.msg, RegisterActivity.this);
                    break;
                case StatusCode.SMS_CODE_RESPONSE_SUCCESS:
                    Utilities.showToast(bean.msg, RegisterActivity.this);
                    if(st==0) {
                        sessionId = bean.body.sessionId;
                    }
                    break;
                case StatusCode.REGISTER_RESPONSE_SUCCESS:
                    Utilities.showToast(bean.msg,RegisterActivity.this);
                    if(st==0){
                        Toast.makeText(RegisterActivity.this,"注册成功",Toast.LENGTH_LONG).show();
                        finish();
                    }
                    break;
            }
            stopProgress();
        }
    };

    @Override
    protected int getLayoutIds() {
        return R.layout.activity_register;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        etPhone = (AppCompatEditText) findViewById(R.id.et_register_phone);
        etPwd = (AppCompatEditText) findViewById(R.id.et_register_pwd);
        etSmsCode = (AppCompatEditText) findViewById(R.id.et_register_sms_code);
        btGetSmsCode = (TimeButton) findViewById(R.id.bt_get_sms_code);
        cbPrivacy = (AppCompatCheckBox) findViewById(R.id.cb_privacy);
        tvPrivacyContetent = (AppCompatTextView) findViewById(R.id.tv_privacy_content);
        btRegisterUpload = (AppCompatButton) findViewById(R.id.bt_register_upload);

        btGetSmsCode.setOnClickListener(this);
        tvPrivacyContetent.setOnClickListener(this);
        btRegisterUpload.setOnClickListener(this);
        cbPrivacy.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    btRegisterUpload.setEnabled(true);
                }else{
                    btRegisterUpload.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_get_sms_code:
                String phone = etPhone.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    Utilities.showToast("手机号不能为空", this);
                    return;
                }
                if (!Utilities.isMobileNO(phone)) {
                    Utilities.showToast("手机号格式不对", this);
                    return;
                }
                getSmsCode(phone);
                break;
            case R.id.tv_privacy_content:
                showPrivacyPop();
                break;
            case R.id.bt_register_upload:
                String ph=etPhone.getText().toString().trim();
                if(TextUtils.isEmpty(ph)){
                    Utilities.showToast("手机号不能为空",this);
                    return;
                }
                if(!Utilities.isMobileNO(ph)){
                    Utilities.showToast("手机号格式不对",this);
                    return;
                }
                String code=etSmsCode.getText().toString().trim();
                if(TextUtils.isEmpty(code)){
                    Utilities.showToast("验证码不能为空",this);
                    return;
                }
                String pwd=etPwd.getText().toString().trim();
                if(TextUtils.isEmpty(pwd)){
                    Utilities.showToast("密码不能为空",this);
                    return;
                }
                if(pwd.length()<=5){
                    Utilities.showToast("密码长度不能小于6位",this);
                    return;
                }
                startUploadRegisterInfo(ph,code,pwd);
                break;
        }
    }

    private void startUploadRegisterInfo(String phone, String code, String pwd) {
        startProgress("注册中...");
        Requester requester = new Requester();
        requester.cmd =4;
        requester.body.put("phone", phone);
        requester.body.put("sms_code", code);
        requester.body.put("pwd",pwd);
        requester.body.put("sessionId",sessionId);
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
                    msg.what = StatusCode.REGISTER_RESPONSE_SUCCESS;
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

    private void showPrivacyPop() {
        if (privacyPopup == null) {
            privacyPopup = new PopupWindow(this);
            WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
            int width = wm.getDefaultDisplay().getWidth();
            int height = wm.getDefaultDisplay().getHeight();
            View contentView = getLayoutInflater().inflate(R.layout.popup_privacy_content, null);
            privacyPopup.setWidth(width / 4*3);
            privacyPopup.setHeight(height / 2);
            privacyPopup.setContentView(contentView);
            privacyPopup.setOutsideTouchable(true);
            PopupWindowCompat.showAsDropDown(privacyPopup, cbPrivacy, 0, 0, Gravity.NO_GRAVITY);
        } else {
            PopupWindowCompat.showAsDropDown(privacyPopup, cbPrivacy, 0, 0, Gravity.NO_GRAVITY);
        }
    }

    private void getSmsCode(String phone) {
        Requester requester = new Requester();
        requester.cmd = 3;
        requester.body.put("type", "0");
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
                    msg.what = StatusCode.SMS_CODE_RESPONSE_SUCCESS;
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
