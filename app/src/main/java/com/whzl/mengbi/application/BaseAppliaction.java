package com.whzl.mengbi.application;

import android.app.Application;

/**
 *@function 1.他是整个程序的入口，2初始化工作，3为整个应用的其他模块提供上下文
 */
public class BaseAppliaction extends Application{

    private static BaseAppliaction instace = null;

    private int userId = 0;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instace = this;
    }

    public static BaseAppliaction getInstance(){
        return instace;
    }
}
