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
import com.overtech.lenovo.config.StatusCode;
import com.overtech.lenovo.config.SystemConfig;
import com.overtech.lenovo.debug.Logger;
import com.overtech.lenovo.entity.RequestExceptBean;
import com.overtech.lenovo.entity.Requester;
import com.overtech.lenovo.entity.ResponseExceptBean;
import com.overtech.lenovo.entity.common.Common;
import com.overtech.lenovo.http.webservice.UIHandler;
import com.overtech.lenovo.utils.SMSCodeCountDownTimer;
import com.overtech.lenovo.utils.StackManager;
import com.overtech.lenovo.utils.Utilities;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

/**
 * Created by Overtech on 16/5/5.
 */
public class FindbackPasswordActivity extends BaseActivity implements View.OnClickListener {
    private AppCompatEditText etFindbackPhone;
    private AppCompatEditText etSmsCode;
    private AppCompatButton btGetSmsCode;
    private AppCompatButton btFindbackUpload;
    private SMSCodeCountDownTimer timer;
    private String sessionId;
    private String ph;
    private UIHandler uiHandler = new UIHandler(this) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String json = (String) msg.obj;
            Logger.e("findbackactivity===" + json);
            if (json == null) {
                return;
            }
            Common bean = gson.fromJson(json, Common.class);
            int st = bean.st;
            switch (msg.what) {
                case StatusCode.FAILED:
                    Utilities.showToast(bean.msg, FindbackPasswordActivity.this);
                    break;
                case StatusCode.SERVER_EXCEPTION:
                    Utilities.showToast(bean.msg, FindbackPasswordActivity.this);
                    break;
                case StatusCode.SMS_CODE_RESPONSE_SUCCESS:
                    Utilities.showToast(bean.msg, FindbackPasswordActivity.this);
                    if (st == 0) {
                        btFindbackUpload.setEnabled(true);
                        sessionId = bean.body.sessionId;
                    }
                    break;
                case StatusCode.FINDBACK_PASSWORD_SUBMIT_SMSCODE_SUCCESS:
                    Utilities.showToast(bean.msg, FindbackPasswordActivity.this);
                    if (st == 0) {
                        Intent intent = new Intent(FindbackPasswordActivity.this, ResetPasswordActivity.class);
                        intent.putExtra("phone",ph);
                        startActivity(intent);
                    }
                    break;
            }
        }
    };

    @Override
    protected int getLayoutIds() {
        return R.layout.activity_findback_password;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Logger.e("findbackPassowrdActivity======onDestroy");
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        StackManager.getStackManager().pushActivity(this);
        etFindbackPhone = (AppCompatEditText) findViewById(R.id.et_findback_phone);
        etSmsCode = (AppCompatEditText) findViewById(R.id.et_findback_sms_code);
        btGetSmsCode = (AppCompatButton) findViewById(R.id.bt_get_sms_code);
        btFindbackUpload = (AppCompatButton) findViewById(R.id.bt_findback_upload);

        btGetSmsCode.setOnClickListener(this);
        btFindbackUpload.setOnClickListener(this);

        timer = new SMSCodeCountDownTimer(60 * 1000, 1000, btGetSmsCode);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_get_sms_code:
                String phone = etFindbackPhone.getText().toString().trim();
                if (TextUtils.isEmpty(phone)) {
                    Utilities.showToast("手机号不能为空", this);
                    return;
                }
                if (!Utilities.isMobileNO(phone)) {
                    Utilities.showToast("手机号格式不对", this);
                    return;
                }
                timer.start();
                getSmsCode(phone);
                break;
            case R.id.bt_findback_upload:
                ph = etFindbackPhone.getText().toString().trim();
                if (TextUtils.isEmpty(ph)) {
                    Utilities.showToast("手机号不能为空", this);
                    return;
                }
                if (!Utilities.isMobileNO(ph)) {
                    Utilities.showToast("手机号格式不对", this);
                    return;
                }
                String code = etSmsCode.getText().toString().trim();
                if (TextUtils.isEmpty(code)) {
                    Utilities.showToast("验证码不能为空", this);
                    return;
                }

                startUploadFindbackInfo(ph, code);
                break;
        }
    }

    private void startUploadFindbackInfo(String phone, String code) {
        Requester requester = new Requester();
        requester.cmd = 5;
        requester.body.put("phone", phone);
        requester.body.put("sms_code", code);
        requester.body.put("sessionId", sessionId);
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
                    msg.what = StatusCode.FINDBACK_PASSWORD_SUBMIT_SMSCODE_SUCCESS;
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

    private void getSmsCode(String phone) {
        Requester requester = new Requester();
        requester.cmd = 3;
        requester.body.put("type", "1");
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        StackManager.getStackManager().popActivity(this);
        timer.cancel();
    }
}
