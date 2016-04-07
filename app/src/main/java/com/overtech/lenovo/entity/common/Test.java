package com.overtech.lenovo.entity.common;

/**
 * Created by Tony1213 on 16/4/7.
 */
public class Test {
//    {"desc":"Login","os":"android","ver":"v1.0","language":"zh",
//    "cmd":1,"uid":"admin","pwd":"12345","tenantcode":"test"}
    private String desc;
    private String os;
    private String ver;
    private String language;
    private String cmd;
    private String uid;
    private String pwd;
    private String tenantcode;

    public String getOs() {
        return os;
    }

    public void setOs(String os) {
        this.os = os;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getVer() {
        return ver;
    }

    public void setVer(String ver) {
        this.ver = ver;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getTenantcode() {
        return tenantcode;
    }

    public void setTenantcode(String tenantcode) {
        this.tenantcode = tenantcode;
    }
}
