package com.whzl.mengbi.ui.common;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.lljjcoder.style.citylist.utils.CityListLoader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.DefaultRefreshFooterCreator;
import com.scwang.smartrefresh.layout.api.DefaultRefreshHeaderCreator;
import com.scwang.smartrefresh.layout.api.RefreshFooter;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.whzl.mengbi.R;
import com.whzl.mengbi.config.SDKConfig;

/**
 * @function 1.他是整个程序的入口，2初始化工作，3为整个应用的其他模块提供上下文
 */
public class BaseApplication extends Application {

    private static BaseApplication instance = null;


    {
        //初始化微信登录
        PlatformConfig.setWeixin(SDKConfig.KEY_WEIXIN, SDKConfig.SECREAT_WEIXING);
        //初始化QQ登录
        PlatformConfig.setQQZone(SDKConfig.KEY_QQ, SDKConfig.SECREAT_QQ);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initUM();
        /**
         * 预先加载一级列表显示 全国所有城市市的数据
         */
        CityListLoader.getInstance().loadCityData(this);
    }

    public static BaseApplication getInstance() {
        return instance;
    }

    /**
     * 初始化友盟
     */
    private void initUM() {
        /**
         * 设置组件化的Log开关
         * 参数: boolean 默认为false，如需查看LOG设置为true
         */
        UMConfigure.setLogEnabled(true);
        UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(true);
        UMShareAPI.get(this).setShareConfig(config);
        UMConfigure.init(this, SDKConfig.KEY_UMENG, "umeng", UMConfigure.DEVICE_TYPE_PHONE, "");

    }

    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.colorAccent, android.R.color.white);//全局设置主题颜色
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }
}
