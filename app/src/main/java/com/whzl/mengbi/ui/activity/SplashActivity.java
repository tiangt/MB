package com.whzl.mengbi.ui.activity;

import android.content.Intent;
import android.text.TextUtils;

import com.whzl.mengbi.R;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.model.entity.CheckLoginResultBean;
import com.whzl.mengbi.model.entity.VisitorUserInfo;
import com.whzl.mengbi.ui.activity.base.BaseActivityNew;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.RxPermisssionsUitls;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;

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


    @Override
    protected void setupContentView() {
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void setupView() {

    }

    @Override
    protected void loadData() {
        HashMap<String, String> paramsMap = new HashMap<>();
        int userId = (int) SPUtils.get(this, SpConfig.KEY_USER_ID, 0);
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
        paramsMap.put("platform", "ANDROID");
        String deviceId = RxPermisssionsUitls.getDevice(this);
        paramsMap.put("deviceNumber", deviceId);
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.USER_VISITOR_LOGIN, RequestManager.TYPE_POST_JSON, paramsMap,
                new RequestManager.ReqCallBack<Object>() {
                    @Override
                    public void onReqSuccess(Object object) {
                        VisitorUserInfo visitorUserInfo = GsonUtils.GsonToBean(object.toString(), VisitorUserInfo.class);
                        if (visitorUserInfo.getCode() == RequestManager.RESPONSE_CODE) {
                            SPUtils.put(BaseApplication.getInstance(), "userId", visitorUserInfo.getData().getUserId());
                            SPUtils.put(BaseApplication.getInstance(), "sessionId", visitorUserInfo.getData().getSessionId());
                            SPUtils.put(BaseApplication.getInstance(), "nickname", visitorUserInfo.getData().getNickname());
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
                .delay(2, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((Boolean s) -> {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    intent.putExtra("isNormalLogin", isNormalLogin);
                    startActivity(intent);
                    finish();
                });
    }
}
