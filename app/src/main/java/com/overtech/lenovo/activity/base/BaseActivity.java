package com.overtech.lenovo.activity.base;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.overtech.lenovo.http.HttpEngine;
import com.overtech.lenovo.http.cookie.PersistentCookieStore;

import java.net.CookieManager;
import java.net.CookiePolicy;

public abstract class BaseActivity extends AppCompatActivity {

    public HttpEngine httpEngine;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        httpEngine=HttpEngine.getInstance();
        httpEngine.initContext(this);
       }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
