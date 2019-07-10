package com.whzl.mengbi.ui.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ImageView;

import com.alibaba.sdk.android.push.AndroidPopupActivity;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.lht.paintview.util.LogUtil;
import com.whzl.mengbi.R;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.model.entity.CheckLoginResultBean;
import com.whzl.mengbi.model.entity.TimeStampInfo;
import com.whzl.mengbi.model.entity.VisitorUserInfo;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.util.DeviceUtils;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.RxPermisssionsUitls;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.glide.GlideImageLoader;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author shaw
 * @date 2018/7/17
 */
public class SplashActivity extends AndroidPopupActivity {


    @BindView(R.id.iv_splash)
    ImageView ivSplash;
    private String deviceId;


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        LogUtils.e("MyMessageReceiver  SplashActivity onCreate");
        // 避免从桌面启动程序后，会重新实例化入口类的activity
        if (!this.isTaskRoot()) {
            Intent intent = getIntent();
            if (intent != null) {
                String action = intent.getAction();
                if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(action)) {
                    finish();
                    return;
                }
            }
        }
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        String file = (String) SPUtils.get(this, SpConfig.START_PAGE_FILE, "");
        if (!TextUtils.isEmpty(file)) {
            GlideImageLoader.getInstace().displayImage(this, Uri.fromFile(new File(file)), ivSplash);
        }

        loadData();
    }

//    @Override
//    protected void setupContentView() {
//        setContentView(R.layout.activity_splash);
//    }

//    @Override
//    protected void setupView() {
//    }

    //    @Override
    protected void loadData() {
        getTimeDiff();
        HashMap<String, String> paramsMap = new HashMap<>();
        long userId = Long.parseLong(SPUtils.get(BaseApplication.getInstance(), SpConfig.KEY_USER_ID, (long) 0).toString());
        String sessionId = (String) SPUtils.get(this, SpConfig.KEY_SESSION_ID, "");
        if (userId == 0 || TextUtils.isEmpty(sessionId)) {
            visitorLogin();
            return;
        }
        paramsMap.put("userId", userId + "");
        if (PushServiceFactory.getCloudPushService().getDeviceId() != null) {
            paramsMap.put("deviceNumber", PushServiceFactory.getCloudPushService().getDeviceId());
        }
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.CHECK_LOGIN, RequestManager.TYPE_POST_JSON, paramsMap,
                new RequestManager.ReqCallBack<Object>() {
                    @Override
                    public void onReqSuccess(Object object) {
                        CheckLoginResultBean resultBean = GsonUtils.GsonToBean(object.toString(), CheckLoginResultBean.class);
                        if (resultBean.code == RequestManager.RESPONSE_CODE) {
                            if (resultBean.data.isLogin) {
                                delayJumpToHomeActivity(true);
                            } else {
                                visitorLogin();
                            }
                        } else {
                            visitorLogin();
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        LogUtils.e("onReqFailed" + errorMsg.toString());
                        visitorLogin();
                    }
                });
    }

    private void visitorLogin() {
        HashMap paramsMap = new HashMap();
        paramsMap.put("platform", RequestManager.CLIENTTYPE);
        RxPermisssionsUitls.getDevice(this, new RxPermisssionsUitls.OnPermissionListener() {
            @Override
            public void onGranted() {
                deviceId = DeviceUtils.getDeviceId(SplashActivity.this);
                paramsMap.put("deviceNumber", deviceId);
                visitorLogin(paramsMap);
            }

            @Override
            public void onDeny() {
                paramsMap.put("deviceNumber", "");
                visitorLogin(paramsMap);
            }
        });
    }

    private void visitorLogin(HashMap paramsMap) {
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.USER_VISITOR_LOGIN, RequestManager.TYPE_POST_JSON, paramsMap,
                new RequestManager.ReqCallBack<Object>() {
                    @Override
                    public void onReqSuccess(Object object) {
                        VisitorUserInfo visitorUserInfo = GsonUtils.GsonToBean(object.toString(), VisitorUserInfo.class);
                        if (visitorUserInfo.getCode() == RequestManager.RESPONSE_CODE) {
                            SPUtils.put(BaseApplication.getInstance(), SpConfig.KEY_USER_ID, visitorUserInfo.getData().getUserId());
                            SPUtils.put(BaseApplication.getInstance(), SpConfig.KEY_SESSION_ID, visitorUserInfo.getData().getSessionId());
                            SPUtils.put(BaseApplication.getInstance(), SpConfig.KEY_USER_NAME, visitorUserInfo.getData().getNickname());
                            SPUtils.put(BaseApplication.getInstance(), SpConfig.KEY_HAS_RECHARGED, false);
                            delayJumpToFirstActivity();
                        } else {
                            delayJumpToFirstActivity();
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        delayJumpToFirstActivity();
                    }
                });
    }

    @SuppressLint("CheckResult")
    private void delayJumpToFirstActivity() {
//        Observable.just(isNormalLogin)
//                .delay(1, TimeUnit.SECONDS)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe((Boolean s) -> {
//                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
//                    intent.putExtra("isNormalLogin", isNormalLogin);
//                    startActivity(intent);
//                    finish();
//                });

        Observable.timer(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    Intent intent = new Intent(SplashActivity.this, FirstActivity.class);
                    startActivity(intent);
                    finish();
                });
    }

    @SuppressLint("CheckResult")
    private void delayJumpToHomeActivity(boolean isNormalLogin) {
        Observable.just(isNormalLogin)
                .delay(1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((Boolean s) -> {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.putExtra("isNormalLogin", isNormalLogin);
                    startActivity(intent);
                    finish();
                });
    }

    private void getTimeDiff() {
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.SITE_TIME, RequestManager.TYPE_GET, new HashMap<>(),
                new RequestManager.ReqCallBack<Object>() {
                    @Override
                    public void onReqSuccess(Object object) {
                        TimeStampInfo timeStampInfo = GsonUtils.GsonToBean(object.toString(), TimeStampInfo.class);
                        if (timeStampInfo.code == 200) {
                            long time = timeStampInfo.data.time;
                            SPUtils.put(BaseApplication.getInstance(), SpConfig.TIME_DIFF, time - System.currentTimeMillis() / 1000);
                            LogUtil.d(time - System.currentTimeMillis() / 1000 + "==============================");
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        delayJumpToHomeActivity(false);
                    }
                });
    }

    @Override
    protected void onSysNoticeOpened(String s, String s1, Map<String, String> map) {
//        String proramId = map.get("programId");
//        startActivity(new Intent(this, MainActivity.class)
//                .putExtra("programId", proramId));
//        finish();
        LogUtils.e("MyMessageReceiver  onSysNoticeOpened");
    }
}
