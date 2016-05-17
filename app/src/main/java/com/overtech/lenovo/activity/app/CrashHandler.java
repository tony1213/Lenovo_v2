package com.overtech.lenovo.activity.app;

import android.content.Context;

/**
 * 处理未捕获异常
 * Created byOvertech  on 16/5/17.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private Context ctx;
    public static CrashHandler crashHandler;

    public static CrashHandler getInstance() {
        if (crashHandler == null) {
            crashHandler = new CrashHandler();
        }
        return crashHandler;
    }

    public void init(Context context) {
        Thread.setDefaultUncaughtExceptionHandler(crashHandler);
        ctx = context;
    }

    @Override
    public void uncaughtException(Thread thread, final Throwable ex) {
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
