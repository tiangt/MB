package com.whzl.mengbi.ui.common;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.net.http.HttpResponseCache;
import android.support.multidex.MultiDex;
import android.text.TextUtils;
import android.util.Log;

import com.baidu.mobstat.StatService;
import com.github.yuweiguocn.library.greendao.MigrationHelper;
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
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.config.SDKConfig;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.gen.DaoMaster;
import com.whzl.mengbi.gen.DaoSession;
import com.whzl.mengbi.greendao.MyOpenHelper;
import com.whzl.mengbi.model.entity.AppDataBean;
import com.whzl.mengbi.ui.activity.MainActivity;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.network.URLContentUtils;
import com.whzl.mengbi.util.network.retrofit.ApiFactory;
import com.whzl.mengbi.util.network.retrofit.ApiObserver;
import com.whzl.mengbi.util.network.retrofit.ParamsUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
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
        //初始化新浪微博
        PlatformConfig.setSinaWeibo(SDKConfig.KEY_SINA, SDKConfig.SECREAT_SINA, "http://sns.whalecloud.com");
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
//        _refWatcher = LeakCanary.install(this);
        CrashHandler.getInstance().init(this);
        initUM();
        initApi();
        initBaiduStatistic();
        initGreenDao();
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        int heapSize = manager.getMemoryClass();
        int maxHeapSize = manager.getLargeMemoryClass();
        initUrl();
        try {
            File cacheDir = new File(getApplicationContext().getCacheDir(), "http");
            HttpResponseCache.install(cacheDir, 1024 * 1024 * 128);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private DaoSession daoSession;

    public DaoSession getDaoSession() {
        return daoSession;
    }

    private void initGreenDao() {
        MigrationHelper.DEBUG = false;
        MyOpenHelper helper = new MyOpenHelper(this, "common_gift.db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }


    private void initBaiduStatistic() {
        StatService.setDebugOn(true);
        StatService.setAppChannel(this, channel, true);
        StatService.autoTrace(this, true, false);
//        StatService.start(this);
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

    private void initUrl() {
        ApiFactory.getInstance().getApi(Api.class)
                .getImgUrl(ParamsUtils.getSignPramsMap(new HashMap<>()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<AppDataBean>() {
                    @Override
                    public void onSuccess(AppDataBean appDataBean) {
                        if (appDataBean == null) {
                            return;
                        }
                        SPUtils.put(instance, SpConfig.REDPACKETURL, appDataBean.redpacketUrl);
                        if (appDataBean != null && appDataBean.newUserAward != null) {
                            if (!TextUtils.isEmpty(appDataBean.newUserAward.guestUserAward)) {
                                SPUtils.put(instance, SpConfig.AWARD_SHOW_TIME, System.currentTimeMillis());
                            }
                        }
                    }

                    @Override
                    public void onError(int code) {
                    }
                });
    }
}
