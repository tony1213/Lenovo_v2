package com.overtech.lenovo.activity.business.common;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.widget.TextView;

import com.overtech.lenovo.R;
import com.overtech.lenovo.activity.MainActivity;
import com.overtech.lenovo.activity.base.BaseActivity;
import com.overtech.lenovo.utils.SharePreferencesUtils;
import com.overtech.lenovo.utils.SharedPreferencesKeys;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by Overtech on 16/4/7.
 */
public class WelcomeActivity extends BaseActivity {
    private TextView tvVersion;
    private boolean isFirstLogin;

    @Override
    protected int getLayoutIds() {
        return R.layout.activity_welcome;
    }

    @Override
    protected void afterCreate(Bundle savedInstanceState) {
        tvVersion = (TextView) findViewById(R.id.tv_version);
        isFirstLogin = (boolean) SharePreferencesUtils.get(this, SharedPreferencesKeys.FIRSTLOGIN, true);
        PackageManager pm = getPackageManager();
        try {
            PackageInfo info = pm.getPackageInfo(getPackageName(), 0);
            String versionName = info.versionName;
            int versionCode = info.versionCode;
            tvVersion.setText(versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isFirstLogin) {
                    Intent intent = new Intent(WelcomeActivity.this, SplashActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    String uid = (String) SharePreferencesUtils.get(WelcomeActivity.this, SharedPreferencesKeys.UID, "");
//                    Logger.e(uid);
                    if (TextUtils.isEmpty(uid)) {
                        Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            }
        }, 3000);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
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
