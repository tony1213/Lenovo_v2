package com.overtech.lenovo.config;

/**
 * Created by Overtech on 16/4/18.
 * 配置handler的what
 */
public class StatusCode {
    public static final int FAILED=0x1;
    public static final int SERVER_EXCEPTION=0x2;
    public static final int WORKORDER_ALL_SUCCESS=0x10;
    public static final int WORKORDER_RECEIVE_SUCCESS=0x11;
    public static final int WORKORDER_APPOINT_SUCCESS=0x12;
    public static final int WORKORDER_HOME_SUCCESS=0x13;
    public static final int WORKORDER_ACCOUNT_SUCCESS=0x14;
    public static final int WORKORDER_EVALUATE_SUCCSS=0x15;

    public static final int WORKORDER_RECEIVE_ACTION_SUCCESS=0x20;
    public static final int WORKORDER_APPOINT_ACTION_SUCCESS=0x21;
    public static final int WORKORDER_HOME_ACTION_SUCCESS=0x22;
}
