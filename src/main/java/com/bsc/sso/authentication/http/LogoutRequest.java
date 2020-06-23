package com.bsc.sso.authentication.http;

import org.json.JSONObject;

public class LogoutRequest {

    private String sysUserName;
    private String sysPassword;

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

    private JSONObject parseJSON() {
        JSONObject request = new JSONObject();
        request.put("SysUserName", this.sysUserName);
        request.put("SysPassword", this.sysPassword);
        return request;
    }

    @Override
    public String toString() {
        return parseJSON().toString();
    }
}
