package com.overtech.lenovo.activity.business.controller;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.overtech.lenovo.R;
import com.overtech.lenovo.activity.base.BaseActivity;
import com.overtech.lenovo.activity.business.common.password.ResetPasswordActivity;
import com.overtech.lenovo.activity.business.common.register.RegisterSetPasswordActivity;
import com.overtech.lenovo.config.Projects;
import com.overtech.lenovo.utils.Utilities;
import com.overtech.lenovo.widget.EditTextWithDelete;
import com.overtech.lenovo.widget.TimeButton;

/*
*@author Tony
*@description 短信验证码
*/
public class GetSmsCodeAndValicateActivity extends BaseActivity implements View.OnClickListener {

    private TextView mTitleContent;
    private ImageView mDoBack;
    private EditTextWithDelete mPhoneNo;
    private EditTextWithDelete mValicateCode;
    private TimeButton mTimeButton;
    private Button mDoNext;
    private String mExtraData;

    @Override
    protected int getLayoutIds() {
        return R.layout.activity_sms_code;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        mTitleContent = (TextView) findViewById(R.id.tv_common_title_content);
        mDoBack = (ImageView) findViewById(R.id.iv_common_title_back);
        mPhoneNo = (EditTextWithDelete) findViewById(R.id.et_sms_code_phone);
        mValicateCode = (EditTextWithDelete) findViewById(R.id.et_valicate_code);
        mTimeButton = (TimeButton) findViewById(R.id.btn_get_valicate_code);
        mDoNext = (Button) findViewById(R.id.btn_valicate_sms_code);

        getExtraData();

        mTitleContent.setText(R.string.common_title_get_sms_code);
        mDoBack.setOnClickListener(this);
        mTimeButton.setTextAfter(getString(R.string.common_after_get_sms_code)).setTextBefore(getString(R.string.common_get_sms_code)).setLenght(60 * 1000);
        mDoNext.setOnClickListener(this);
        mPhoneNo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (Utilities.isMobileNO(s.toString())) {
                    mTimeButton.setBackgroundResource(R.drawable.shape_button_visiable);
                    mTimeButton.setEnabled(true);
                } else {
                    mTimeButton.setBackgroundResource(R.drawable.shape_button_disable);
                    mTimeButton.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void getExtraData() {
        Intent intent = getIntent();
        if (null == intent) {
            return;
        }
        mExtraData = intent.getStringExtra("flag");
        if (TextUtils.equals("", mExtraData)) {
            return;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_common_title_back:
                this.finish();
                break;
            case R.id.btn_valicate_sms_code:
                Intent intent = new Intent();
                if (TextUtils.equals(mExtraData, Projects.LOST_PASSWORD)) {
                    intent.setClass(this, ResetPasswordActivity.class);
                } else if (TextUtils.equals(mExtraData, Projects.REGISTER)) {
                    intent.setClass(this, RegisterSetPasswordActivity.class);
                } else {
                    return;
                }
                startActivity(intent);
                break;
        }
    }
}
