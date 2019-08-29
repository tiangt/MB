package com.whzl.mengbi.model.impl;


import com.whzl.mengbi.model.LoginModel;
import com.whzl.mengbi.model.entity.VisitorUserInfo;
import com.whzl.mengbi.model.entity.UserInfo;
import com.whzl.mengbi.presenter.OnLoginFinishedListener;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;

import java.util.HashMap;


/**
 * Class Note:登录，如果名字或者密码为空则登录失败，否则登录成功
 */
public class LoginModelImpl implements LoginModel {


    /**
     * 用户登录
     *
     * @param listener
     */
    @Override
    public void doLogin(HashMap hashMap, final OnLoginFinishedListener listener) {
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.USER_NORMAL_LOGIN, RequestManager.TYPE_POST_JSON, hashMap,
                new RequestManager.ReqCallBack<Object>() {
                    @Override
                    public void onReqSuccess(Object result) {
                        UserInfo userInfo = GsonUtils.GsonToBean(result.toString(), UserInfo.class);
                        if (userInfo.getCode() == RequestManager.RESPONSE_CODE) {
                            listener.onLoginSuccess(userInfo);
                        } else if (userInfo.getCode() == -1208) {
                            listener.onError("系统繁忙");
                        } else {
                            listener.onError(userInfo.getMsg());
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        listener.onError(errorMsg);
                    }
                });
    }


    /**
     * 三方登录
     *
     * @param listener
     */

    @Override
    public void openLogin(HashMap hashMap, OnLoginFinishedListener listener) {
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.OPEN_LOGIN, RequestManager.TYPE_POST_JSON, hashMap,
                new RequestManager.ReqCallBack<Object>() {
                    @Override
                    public void onReqSuccess(Object result) {
                        UserInfo userInfo = GsonUtils.GsonToBean(result.toString(), UserInfo.class);
                        if (userInfo.getCode() == RequestManager.RESPONSE_CODE) {
                            listener.onLoginSuccess(userInfo);
                        } else {
                            listener.onError(userInfo.getMsg());
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        listener.onError(errorMsg);
                    }
                });
    }
}
