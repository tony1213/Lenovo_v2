package com.overtech.lenovo.activity.base;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.overtech.lenovo.http.HttpEngine;
import com.overtech.lenovo.widget.dialogeffects.NiftyDialogBuilder;



public abstract class BaseActivity extends AppCompatActivity {
    public NiftyDialogBuilder dialogBuilder;
    public HttpEngine httpEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(getLayoutIds());
        httpEngine=HttpEngine.getInstance();
        httpEngine.initContext(this);
        afterCreate(savedInstanceState);
        dialogBuilder = NiftyDialogBuilder.getInstance(this);
    }

    protected abstract int getLayoutIds();
    protected abstract void afterCreate(Bundle savedInstanceState);
    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if(dialogBuilder.isShowing()){
            dialogBuilder.dismiss();
        }
    }
}
