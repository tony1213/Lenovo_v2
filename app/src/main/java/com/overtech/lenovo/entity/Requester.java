package com.overtech.lenovo.entity;

import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Overtech Will on 16/4/8.
 */
public class Requester {
    public String os = "android";
    public String ver = "v1.0";
    public String lg = "zh";
    public int cmd;
    public String uid;
    public Map<String, Object> body = new HashMap<String, Object>();


}

