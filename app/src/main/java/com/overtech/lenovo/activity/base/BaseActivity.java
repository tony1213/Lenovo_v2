package com.overtech.lenovo.activity.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.overtech.lenovo.http.HttpEngine;
import com.overtech.lenovo.widget.dialogeffects.NiftyDialogBuilder;
import com.overtech.lenovo.widget.progressdialog.CustomProgressDialog;


public abstract class BaseActivity extends AppCompatActivity {
    public NiftyDialogBuilder dialogBuilder;
    public CustomProgressDialog newFrament;
    public HttpEngine httpEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        httpEngine = HttpEngine.getInstance();
        httpEngine.initContext(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(getLayoutIds());
        dialogBuilder = NiftyDialogBuilder.getInstance(this);
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
        if (newFrament.getDialog().isShowing()) {
            newFrament.dismiss();
        }
    }

    protected abstract int getLayoutIds();

    protected abstract void afterCreate(Bundle savedInstanceState);

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (dialogBuilder.isShowing()) {
            dialogBuilder.dismiss();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
