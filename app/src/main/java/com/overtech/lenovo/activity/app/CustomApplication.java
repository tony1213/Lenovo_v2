package com.overtech.lenovo.activity.app;

import android.app.Application;
import android.content.Context;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.overtech.lenovo.service.LocationService;
import com.overtech.lenovo.widget.bitmap.ImageLoader;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

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
    private RefWatcher refWatcher;

    public static RefWatcher getRefWatcher(Context context) {
        CustomApplication application = (CustomApplication) context.getApplicationContext();
        return application.refWatcher;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        TypefaceProvider.registerDefaultIconSets();
        refWatcher = LeakCanary.install(this);
        ImageLoader.getInstance().initContext(getApplicationContext());
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
                latitude=location.getLatitude();
                longitude=location.getLongitude();
                locationService.stop();
            } else {
                locationService.start();
            }
        }

    }
}
