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
    public static final int WORKORDER_SOLVE_SUCCESS=0x14;
    public static final int WORKORDER_ACCOUNT_SUCCESS=0x15;
    public static final int WORKORDER_EVALUATE_SUCCSS=0x16;
    public static final int WORKORDER_CONTRACT_SUCCESS=0x17;
    public static final int WORKORDER_MSG_SUCCESS=0x18;

    public static final int WORKORDER_SUCCESS=0x20;
    public static final int WORKORDER_RECEIVE_ACTION_SUCCESS=0x21;
    public static final int WORKORDER_APPOINT_ACTION_SUCCESS=0x22;
    public static final int WORKORDER_HOME_ACTION_SUCCESS=0x23;
    public static final int WORKORDER_SOLVE_ACTION_SUCCESS=0x24;
    public static final int WORKORDER_NOTIFICATION_SUCCESS=0x25;

    public static final int WORKORDER_DETAIL_INFORMATION_SUCCESS=0x30;

    public static final int WORKORDER_STORE_INFORMATION_SUCCESS=0x40;
    public static final int WORKORDER_STORE_REMARK_SUCCESS=0x41;
    public static final int WORKORDER_STORE_REMARK_UPLOAD_SUCCESS=0x42;
    public static final int WORKORDER_STORE_REPAIR_INFO_SUCCESS=0x43;

    public static final int WORKORDER_PROPERTY_SUCCESS=0x50;

    public static final int PERSONAL_SUCCESS=0x60;

    public static final int PERSONAL_SETTING_SUCCESS=0x70;
    public static final int PERSONAL_SETTING_UPDATE_SUCCESS=0x71;
    public static final int PERSONAL_SETTING_UPLOAD_ID_POSITIVE=0x72;
    public static final int PERSONAL_SETTING_UPLOAD_ID_OPPOSITE=0x73;

    public static final int KNOWLEDGE_PUBLIC_SUCCESS=0x80;
    public static final int KNOWLEDGE_CONTRACT_SUCCESS=0x81;
    public static final int KNOWLEDGE_CONTENT_SUCCESS=0x82;

    public static final int INFORMATION_SUCCESS=0x90;
    public static final int INFORMATION_COMMENT_SUCCESS=0x91;
}
