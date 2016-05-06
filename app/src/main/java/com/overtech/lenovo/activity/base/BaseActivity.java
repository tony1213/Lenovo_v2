package com.overtech.lenovo.activity.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.overtech.lenovo.http.HttpEngine;
import com.overtech.lenovo.widget.dialog.CustomProgressDialog;


public abstract class BaseActivity extends AppCompatActivity {
    public CustomProgressDialog newFrament;
    public HttpEngine httpEngine;
    public Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        httpEngine = HttpEngine.getInstance();
        httpEngine.initContext(getApplicationContext());
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(getLayoutIds());
        afterCreate(savedInstanceState);

    }

    public void startProgress(String content) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment pre = getSupportFragmentManager().findFragmentByTag("custom");
        if (pre != null) {
            ft.remove(pre);
        }
        ft.addToBackStack(null);
        newFrament = CustomProgressDialog.newInstance(content);
        newFrament.show(ft, "custom");

    }


    public void stopProgress() {
        if (newFrament != null) {
            if (newFrament.getDialog() != null) {
                if (newFrament.getDialog().isShowing()) {
                    newFrament.dismiss();
                }
            }
        }
    }

    protected abstract int getLayoutIds();

    protected abstract void afterCreate(Bundle savedInstanceState);

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
