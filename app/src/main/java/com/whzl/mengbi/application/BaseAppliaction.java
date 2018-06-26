package com.whzl.mengbi.application;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.google.zxing.multi.qrcode.detector.MultiDetector;

/**
 *@function 1.他是整个程序的入口，2初始化工作，3为整个应用的其他模块提供上下文
 */
public class BaseAppliaction extends Application{

    private static BaseAppliaction instace = null;

    private int userId = 0;

    private  String sessionId;

    private String token;

    private boolean islogin = false;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instace = this;
    }

    public static BaseAppliaction getInstance(){
        return instace;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }

    public boolean isIslogin() {
        return islogin;
    }
    public void setIslogin(boolean islogin) {
        this.islogin = islogin;
    }
}
