package com.overtech.lenovo.activity.business.common;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.overtech.lenovo.R;
import com.overtech.lenovo.activity.base.BaseActivity;
import com.overtech.lenovo.utils.SharePreferencesUtils;
import com.overtech.lenovo.utils.SharedPreferencesKeys;

/**
 * Created by Overtech on 16/4/7.
 */
public class WelcomeActivity extends BaseActivity {
    private boolean isFirstLogin;
    @Override
    protected int getLayoutIds() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        isFirstLogin=(boolean)SharePreferencesUtils.get(this, SharedPreferencesKeys.FIRSTLOGIN,true);
        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isFirstLogin) {
                    Intent intent = new Intent(WelcomeActivity.this, SplashActivity.class);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent= new Intent(WelcomeActivity.this,LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 3000);
    }
}
