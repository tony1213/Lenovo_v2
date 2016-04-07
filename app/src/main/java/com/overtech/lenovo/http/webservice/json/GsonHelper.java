package com.overtech.lenovo.http.webservice.json;


import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.overtech.lenovo.http.webservice.anno.Ignore;

public class GsonHelper {
    private Gson mGson;
    private static GsonHelper instance = null;

    ExclusionStrategy strategy = new ExclusionStrategy() {

        @Override
        public boolean shouldSkipField(FieldAttributes arg0) {
            Ignore anno_ignore = arg0.getAnnotation(Ignore.class);
            return anno_ignore != null;
        }

        @Override
        public boolean shouldSkipClass(Class<?> arg0) {
            return false;
        }
    };

    private GsonHelper() {
        mGson = new GsonBuilder().setExclusionStrategies(strategy).disableHtmlEscaping().create();
    }

    public static synchronized GsonHelper getInstance() {
        if (instance == null) {
            instance = new GsonHelper();
        }
        return instance;
    }

    public Gson getGson() {
        return mGson;
    }
}
