package com.overtech.lenovo.activity.app;

import android.app.Application;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.overtech.lenovo.service.LocationService;

/**
 * Created by Tony1213 on 16/3/23.
 */
public class CustomApplication extends Application {

    //    public static Map<String,Long> map;
    public LocationService locationService;
    public MyBDLocaitonListener listener;
    public String city;

    @Override
    public void onCreate() {
        super.onCreate();
        TypefaceProvider.registerDefaultIconSets();

        locationService = new LocationService(getApplicationContext());
        listener = new MyBDLocaitonListener();
        locationService.registerListener(listener);
        locationService.start();
    }

    class MyBDLocaitonListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (location != null) {
                city = location.getCity();
                locationService.stop();
            } else {
                locationService.start();
            }
        }

    }
}
