package com.whzl.mengbi.ui.common;

import android.app.Activity;
import android.app.Application;
import android.app.FragmentManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.multidex.MultiDex;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.lljjcoder.style.citylist.utils.CityListLoader;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.whzl.mengbi.R;

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
        initUM();
        /**
         * 预先加载一级列表显示 全国所有城市市的数据
         */
        CityListLoader.getInstance().loadCityData(this);
    }

    public static BaseAppliaction getInstance(){
        return instace;
    }


    /**
     * 初始化友盟
     */
    private void initUM(){
        /**
         * 设置组件化的Log开关
         * 参数: boolean 默认为false，如需查看LOG设置为true
         */
        UMConfigure.setLogEnabled(true);
        UMConfigure.init(this,UMConfigure.DEVICE_TYPE_PHONE,"5b3c9412a40fa34d390000ac");
        //初始化微信登录
        PlatformConfig.setWeixin("wxbaa94b521372015c", "c1762807092c6ace7405071a9df51290");
        //初始化QQ登录
        PlatformConfig.setQQZone("101477481", "52f84675951166e7dcc95b019ccd63fa");
        UMShareAPI.get(this);
    }

}
