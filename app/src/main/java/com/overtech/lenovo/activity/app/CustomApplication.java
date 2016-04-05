package com.overtech.lenovo.activity.app;

import android.app.Application;

import com.beardedhen.androidbootstrap.TypefaceProvider;

/**
 * Created by Tony1213 on 16/3/23.
 */
public class CustomApplication extends Application {

//    public static Map<String,Long> map;

    @Override
    public void onCreate() {
        super.onCreate();
        TypefaceProvider.registerDefaultIconSets();
    }
}
