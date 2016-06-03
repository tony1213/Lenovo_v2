package com.overtech.lenovo.activity.business.common.password;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.overtech.lenovo.R;
import com.overtech.lenovo.activity.base.BaseActivity;
import com.overtech.lenovo.activity.business.common.LoginActivity;
import com.overtech.lenovo.utils.Utilities;
import com.umeng.analytics.MobclickAgent;

/*
*@author Tony
*@description 重置密码成功
*/
public class ResetPasswordSuccessActivity extends BaseActivity {
    private TextView mTitleContent;
    private ImageView mDoBack;

    @Override
    protected int getLayoutIds() {
        return R.layout.activity_reset_password_success;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        mTitleContent = (TextView) findViewById(R.id.tv_common_title_content);
        mDoBack = (ImageView) findViewById(R.id.iv_common_title_back);

        mTitleContent.setText(R.string.commom_title_reset_password_success);
        Utilities.showToast("2秒后跳转到登录", this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(ResetPasswordSuccessActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        }, 2000);
        mDoBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
