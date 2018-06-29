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

}
