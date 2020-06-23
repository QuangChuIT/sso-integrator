package com.bsc.sso.authentication.http;

import org.json.JSONObject;

public class LoginUrlRequest {

    private String sysUserName;
    private String sysPassword;
    private String param1;
    private String param2;
    private String param3;
    private String returnUri;

    public String getSysUserName() {
        return sysUserName;
    }

    public void setSysUserName(String sysUserName) {
        this.sysUserName = sysUserName;
    }

    public String getSysPassword() {
        return sysPassword;
    }

    public void setSysPassword(String sysPassword) {
        this.sysPassword = sysPassword;
    }

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public String getParam2() {
        return param2;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }

    public String getParam3() {
        return param3;
    }

    public void setParam3(String param3) {
        this.param3 = param3;
    }

    public String getReturnUri() {
        return returnUri;
    }

    public void setReturnUri(String returnUri) {
        this.returnUri = returnUri;
    }

    private JSONObject parseJSON() {
        JSONObject request = new JSONObject();
        request.put("SysUserName", this.sysUserName);
        request.put("SysPassword", this.sysPassword);
        request.put("Param1", this.param1);
        request.put("Param2", this.param2);
        request.put("Param3", this.param3);
        request.put("Returnuri", this.returnUri);
        return request;
    }

    @Override
    public String toString() {
        return parseJSON().toString();
    }
}
