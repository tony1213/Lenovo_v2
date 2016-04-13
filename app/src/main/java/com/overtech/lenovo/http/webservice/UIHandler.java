package com.overtech.lenovo.http.webservice;

import android.content.Context;
import android.os.Handler;

/**
 * Created by Overtech on 16/4/11.
 */
public class UIHandler extends Handler {
    private Context context;
    public UIHandler(Context context){
        this.context=context;
    }
}
