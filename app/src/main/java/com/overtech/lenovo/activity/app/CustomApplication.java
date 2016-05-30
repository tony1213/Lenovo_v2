package com.overtech.lenovo.activity.app;

import android.app.Application;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.mapapi.SDKInitializer;
import com.overtech.lenovo.service.LocationService;
import com.overtech.lenovo.widget.bitmap.ImageLoader;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by Tony1213 on 16/3/23.
 */
public class CustomApplication extends Application {

    //    public static Map<String,Long> map;
    public LocationService locationService;
    public MyBDLocaitonListener listener;
    public double longitude;
    public double latitude;
    public String city;
    private CrashHandler crashHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(this);
        ImageLoader.getInstance().initContext(getApplicationContext());
        locationService = new LocationService(getApplicationContext());
        listener = new MyBDLocaitonListener();
        locationService.registerListener(listener);
        locationService.start();
//        crashHandler =CrashHandler.getInstance();
//        crashHandler.initContext(getApplicationContext());
//        Thread.setDefaultUncaughtExceptionHandler(crashHandler);//友盟帮助做了，自己可以先不处理
        MobclickAgent.setCatchUncaughtExceptions(true);
    }

    class MyBDLocaitonListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            // TODO Auto-generated method stub
            if (location != null) {
                city = location.getCity();
                latitude = location.getLatitude();
                longitude = location.getLongitude();
                locationService.stop();
            } else {
                locationService.start();
            }
        }

    }
}
