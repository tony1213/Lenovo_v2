package com.overtech.lenovo.activity.base;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

<<<<<<< HEAD
import com.overtech.lenovo.http.HttpEngine;
import com.overtech.lenovo.http.cookie.PersistentCookieStore;

import java.net.CookieManager;
import java.net.CookiePolicy;

public abstract class BaseActivity extends AppCompatActivity {

    public HttpEngine httpEngine;
=======
import com.overtech.lenovo.widget.dialogeffects.NiftyDialogBuilder;

public abstract class BaseActivity extends AppCompatActivity {

    public NiftyDialogBuilder dialogBuilder;
>>>>>>> origin/master

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
<<<<<<< HEAD
        httpEngine=HttpEngine.getInstance();
        httpEngine.initContext(this);
       }
=======
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(getLayoutIds());
        afterCreate(savedInstanceState);
        dialogBuilder = NiftyDialogBuilder.getInstance(this);
    }
>>>>>>> origin/master

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
