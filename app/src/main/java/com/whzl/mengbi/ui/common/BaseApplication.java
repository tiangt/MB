package com.whzl.mengbi.ui.common;

import android.app.ActivityManager;
import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.http.HttpResponseCache;
import android.os.Build;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.alibaba.sdk.android.push.register.HuaWeiRegister;
import com.alibaba.sdk.android.push.register.MiPushRegister;
import com.alibaba.sdk.android.push.register.OppoRegister;
import com.baidu.mobstat.StatService;
import com.github.yuweiguocn.library.greendao.MigrationHelper;
import com.meituan.android.walle.WalleChannelReader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
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
import com.whzl.mengbi.ui.widget.recyclerview.PullRefreshHeader;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.network.URLContentUtils;
import com.whzl.mengbi.util.network.retrofit.ApiFactory;
import com.whzl.mengbi.util.network.retrofit.ApiObserver;
import com.whzl.mengbi.util.network.retrofit.ParamsUtils;

import org.greenrobot.greendao.identityscope.IdentityScopeType;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.plugins.RxJavaPlugins;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.ResponseBody;

/**
 * @author shaw
 * @function 1.他是整个程序的入口，2初始化工作，3为整个应用的其他模块提供上下文
 */
public class BaseApplication extends Application {
    public static int heapSize = 0;

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

        String arch = "";//cpu类型
        try {
            Class<?> clazz = Class.forName("android.os.SystemProperties");
            Method get = clazz.getDeclaredMethod("get", new Class[]{String.class});
            arch = (String) get.invoke(clazz, new Object[]{"ro.product.cpu.abi"});
        } catch (Exception e) {
            e.printStackTrace();
        }
        LogUtils.e("arch  " + arch);

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
        RxJavaPlugins.setErrorHandler(throwable -> {
            //异常处理

        });
//        _refWatcher = LeakCanary.install(this);
//        CrashHandler.getInstance().init(this);
        initUM();
        initApi();
        initBaiduStatistic();
        initGreenDao();
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        heapSize = manager.getMemoryClass();
        initUrl();
        try {
            File cacheDir = new File(getApplicationContext().getCacheDir(), "http");
            HttpResponseCache.install(cacheDir, 1024 * 1024 * 128);
        } catch (IOException e) {
            e.printStackTrace();
        }
        initCloudChannel(this);

    }

    private void initCloudChannel(Context applicationContext) {
        this.createNotificationChannel();
        PushServiceFactory.init(applicationContext);
        CloudPushService pushService = PushServiceFactory.getCloudPushService();
        pushService.register(applicationContext, new CommonCallback() {
            @Override
            public void onSuccess(String response) {
                LogUtils.d("init cloudchannel success");
            }

            @Override
            public void onFailed(String errorCode, String errorMessage) {
                LogUtils.d("init cloudchannel failed -- errorcode:" + errorCode + " -- errorMessage:" + errorMessage);
            }
        });

        String deviceId = pushService.getDeviceId();
        LogUtils.e("ssssssssssssss    " + deviceId);

        // 注册方法会自动判断是否支持小米系统推送，如不支持会跳过注册。
        MiPushRegister.register(applicationContext, "2882303761517841997", "5591784198997");
        // 注册方法会自动判断是否支持华为系统推送，如不支持会跳过注册。
        HuaWeiRegister.register(applicationContext);
        //GCM/FCM辅助通道注册
//        GcmRegister.register(this, sendId, applicationId); //sendId/applicationId为步骤获得的参数
        // OPPO通道注册
        OppoRegister.register(applicationContext, "7c6984275e2d4fe1a96b74a34a261b25", "da4f97d2bba94079912eae24f295486c"); // appKey/appSecret在OPPO通道开发者平台获取
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            // 通知渠道的id
            String id = "1";
            // 用户可以看到的通知渠道的名字.
            CharSequence name = "notification channel";
            // 用户可以看到的通知渠道的描述
            String description = "notification description";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(id, name, importance);
            // 配置通知渠道的属性
            mChannel.setDescription(description);
            // 设置通知出现时的闪灯（如果 android 设备支持的话）
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            // 设置通知出现时的震动（如果 android 设备支持的话）
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            //最后在notificationmanager中创建该通知渠道
            mNotificationManager.createNotificationChannel(mChannel);
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
        daoSession = daoMaster.newSession(IdentityScopeType.None);
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
            return new PullRefreshHeader(context);
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
                LogUtils.e(response.request().url().url().toString() + bodyString);
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
                        SPUtils.put(instance, SpConfig.WEEKSTARHELPURL, appDataBean.weekStarHelpUrl);
                        SPUtils.put(instance, SpConfig.ANCHORAGREEURL, appDataBean.anchorAgreeUrl);
                        SPUtils.put(instance, SpConfig.USERAGREEURL, appDataBean.userAgreeUrl);
                        SPUtils.put(instance, SpConfig.USERGRADEURL, appDataBean.userGradeUrl);
                        SPUtils.put(instance, SpConfig.ANCHORGRADEURL, appDataBean.anchorGradeUrl);
                        SPUtils.put(instance, SpConfig.REDPACKETHELPURL, appDataBean.redpacketHelpUrl);
                        SPUtils.put(instance, SpConfig.GUESSHELP_URL, appDataBean.guessHelpUrl);
                        SPUtils.put(instance, SpConfig.PKQUALIFYINGHELPURL, appDataBean.pkQualifyingHelpUrl);
                        SPUtils.put(instance, SpConfig.LUCKDRAWURL, appDataBean.luckdrawUrl);
                        if (appDataBean.newUserAward != null) {
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
