package com.whzl.mengbi.ui.activity;

import android.content.Intent;
import android.text.TextUtils;

import com.google.gson.JsonElement;
import com.lht.paintview.util.LogUtil;
import com.whzl.mengbi.R;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.model.entity.CheckLoginResultBean;
import com.whzl.mengbi.model.entity.ResponseInfo;
import com.whzl.mengbi.model.entity.TimeStampInfo;
import com.whzl.mengbi.model.entity.VisitorUserInfo;
import com.whzl.mengbi.ui.activity.base.BaseActivityNew;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.util.DeviceUtils;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.RxPermisssionsUitls;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;
import com.whzl.mengbi.util.network.retrofit.ApiFactory;
import com.whzl.mengbi.util.network.retrofit.ApiObserver;
import com.whzl.mengbi.util.network.retrofit.ParamsUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author shaw
 * @date 2018/7/17
 */
public class SplashActivity extends BaseActivityNew {


    private String deviceId;

    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void setupView() {
    }

    @Override
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
                            delayJumpToHomeActivity(false);
                        } else {
                            delayJumpToHomeActivity(false);
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        delayJumpToHomeActivity(false);
                    }
                });
    }

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
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.SITE_TIME, RequestManager.TYPE_POST_JSON, new HashMap<>(),
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
}
