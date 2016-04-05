package com.overtech.lenovo.widget.pulltorefresh;

import java.text.SimpleDateFormat;
import java.util.Date;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

public class RefreshTime {
    final static String PRE_NAME = "refresh_time";
    final static String SET_FRESHTIME = "set_refresh_time";

    public static String getRefreshTime(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(PRE_NAME, Context.MODE_APPEND);
        String lastTime=preferences.getString(SET_FRESHTIME, "");
        if (TextUtils.equals("", lastTime)) {
        	SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy/MM/dd HH:mm");
			lastTime=simpleDateFormat.format(new Date());
		}
        return lastTime;
    }

    public static void setRefreshTime(Context context, String newPasswd) {
        SharedPreferences preferences = context.getSharedPreferences(PRE_NAME, Context.MODE_APPEND);
        Editor editor = preferences.edit();
        editor.putString(SET_FRESHTIME, newPasswd);
        editor.commit();
    }

}