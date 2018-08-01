package com.whzl.mengbi.ui.common;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.baidu.mobstat.StatService;
import com.lht.paintview.util.LogUtil;
import com.meituan.android.walle.WalleChannelReader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.whzl.mengbi.R;
import com.whzl.mengbi.config.SDKConfig;
import com.whzl.mengbi.util.network.URLContentUtils;
import com.whzl.mengbi.util.network.retrofit.ApiFactory;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.ResponseBody;

/**
 * @author shaw
 * @function 1.他是整个程序的入口，2初始化工作，3为整个应用的其他模块提供上下文
 */
public class BaseApplication extends Application {

    private static BaseApplication instance = null;
    public RefWatcher _refWatcher;

    public String getChannel() {
        return channel;
    }

    private String channel;


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
        channel = WalleChannelReader.getChannel(getApplicationContext());
        if (channel == null) {
            channel = "";
        }
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        CrashHandler.getInstance().init(this);
        _refWatcher = LeakCanary.install(this);
        initUM();
        initApi();Reportf
        initBaiduStatistic();
    }

    private void initBaiduStatistic() {
        StatService.setDebugOn(true);
        StatService.setAppChannel(this, channel, true);
        StatService.autoTrace(this, true, false);
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
        UMConfigure.init(this, SDKConfig.KEY_UMENG, channel, UMConfigure.DEVICE_TYPE_PHONE, null);
//        MobclickAgent.setScenarioType(this, MobclickAgent.EScenarioType.E_UM_NORMAL);
//        MobclickAgent.openActivityDurationTrack(false);
//        UMUtils.setChannel(this, channel);

    }

    //static 代码段可以防止内存泄露
    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> {
            layout.setPrimaryColorsId(R.color.appBg);
            return new ClassicsHeader(context);
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> {
            //指定为经典Footer，默认是 BallPulseFooter
            ClassicsFooter classicsFooter = new ClassicsFooter(context);
            classicsFooter.setDrawableSize(20);
            classicsFooter.setPrimaryColorId(R.color.appBg);
            return classicsFooter;
        });
    }

    public void initApi() {
        ApiFactory.getInstance().build(getApplicationContext(), URLContentUtils.getBaseUrl(), (Interceptor.Chain chain) -> {
            Request newRequest = chain.request();
            okhttp3.Response response = chain.proceed(newRequest);
            String bodyString = "";
            if (response.isSuccessful()) {
                bodyString = response.body().string();
                LogUtil.d("okhttp = " + bodyString);
            }
            okhttp3.Response r = response.newBuilder()
                    .body(ResponseBody.create(response.body().contentType(), bodyString))
                    .build();
            return r;
        });
    }
}
