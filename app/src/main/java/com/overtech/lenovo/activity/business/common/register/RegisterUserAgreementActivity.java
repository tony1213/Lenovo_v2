package com.overtech.lenovo.activity.business.common.register;

import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.view.View;

import com.overtech.lenovo.R;
import com.overtech.lenovo.activity.base.BaseActivity;
import com.overtech.lenovo.utils.Utilities;
import com.overtech.lenovo.widget.TimeButton;

public class RegisterUserAgreementActivity extends BaseActivity implements View.OnClickListener {
    private AppCompatEditText etPhone;
    private AppCompatEditText etPwd;
    private AppCompatEditText etSmsCode;
    private TimeButton btGetSmsCode;
    private AppCompatCheckBox cbPrivacy;
    private AppCompatTextView tvPrivacyContetent;
    private AppCompatButton btRegisterUpload;

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
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_get_sms_code:
                String phone=etPhone.getText().toString().trim();
                if(!TextUtils.isEmpty(phone)){
                    Utilities.showToast("手机号不能为空",this);
                    return;
                }
                if(!Utilities.isMobileNO(phone)){
                    Utilities.showToast("手机号格式不对",this);
                }
                break;
            case R.id.tv_privacy_content:

                break;
            case R.id.bt_register_upload:
                break;
        }
    }
}
