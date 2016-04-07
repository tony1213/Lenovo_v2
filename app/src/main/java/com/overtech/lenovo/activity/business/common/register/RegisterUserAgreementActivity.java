package com.overtech.lenovo.activity.business.common.register;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.overtech.lenovo.R;
import com.overtech.lenovo.activity.base.BaseActivity;
import com.overtech.lenovo.activity.business.controller.GetSmsCodeAndValicateActivity;
import com.overtech.lenovo.config.Projects;

public class RegisterUserAgreementActivity extends BaseActivity implements View.OnClickListener {
    private TextView mTitleContent;
    private ImageView mDoBack;
    private Button mDoNext;
    private CheckBox mCheckAgreement;

    @Override
    protected int getLayoutIds() {
        return R.layout.activity_register;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        mTitleContent = (TextView) findViewById(R.id.tv_common_title_content);
        mDoBack = (ImageView) findViewById(R.id.iv_common_title_back);
        mDoNext = (Button) findViewById(R.id.btn_register_do_next_agreement);
        mCheckAgreement = (CheckBox) findViewById(R.id.cb_register_item_privacy);

        mTitleContent.setText(R.string.commom_title_register_user_agreement);
        mDoBack.setOnClickListener(this);
        mDoNext.setOnClickListener(this);
        mCheckAgreement.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mDoNext.setBackgroundResource(R.drawable.shape_button_visiable);
                    mDoNext.setEnabled(true);
                } else {
                    mDoNext.setBackgroundResource(R.drawable.shape_button_disable);
                    mDoNext.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_common_title_back:
                finish();
                break;
            case R.id.btn_register_do_next_agreement:
                Intent intent = new Intent(this, GetSmsCodeAndValicateActivity.class);
                intent.putExtra("flag", Projects.REGISTER);
                startActivity(intent);
                break;
        }
    }
}
