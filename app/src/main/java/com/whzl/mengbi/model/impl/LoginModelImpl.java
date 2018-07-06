package com.whzl.mengbi.model.impl;


import android.text.TextUtils;

import com.whzl.mengbi.model.LoginModel;
import com.whzl.mengbi.model.entity.VisitorUserInfo;
import com.whzl.mengbi.model.entity.UserInfo;
import com.whzl.mengbi.presenter.OnLoginFinishedListener;
import com.whzl.mengbi.ui.common.BaseAppliaction;
import com.whzl.mengbi.util.EncryptUtils;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;

import java.util.HashMap;


/**
 * Class Note:登陆，如果名字或者密码为空则登陆失败，否则登陆成功
 */
public class LoginModelImpl implements LoginModel{

    /**
     * 游客登录
     * @param deviceId
     * @param listener
     */
    @Override
    public void doVisitorLogin(String deviceId, final OnLoginFinishedListener listener) {
        HashMap paramsMap = new HashMap();
        paramsMap.put("platform","ANDROID");
        paramsMap.put("deviceNumber", deviceId);
        RequestManager.getInstance(BaseAppliaction.getInstance()).requestAsyn(URLContentUtils.USER_VISITOR_LOGIN, RequestManager.TYPE_POST_JSON, paramsMap,
                new RequestManager.ReqCallBack<Object>() {
                    @Override
                    public void onReqSuccess(Object result) {
                        VisitorUserInfo visitorUserInfo = GsonUtils.GsonToBean(result.toString(),VisitorUserInfo.class);
                        if(visitorUserInfo.getCode()==200){
                            SPUtils.put(BaseAppliaction.getInstance(),"userId",visitorUserInfo.getData().getUserId());
                            SPUtils.put(BaseAppliaction.getInstance(),"nickname",visitorUserInfo.getData().getNickname());
                            SPUtils.put(BaseAppliaction.getInstance(),"sessionId",visitorUserInfo.getData().getSessionId());
                            SPUtils.put(BaseAppliaction.getInstance(),"islogin",false);
                            listener.onVisitorLoginSuccess();
                        }
                    }
                    @Override
                    public void onReqFailed(String errorMsg) {
                        LogUtils.e("onReqFailed"+errorMsg.toString());
                    }
                });
    }


    /**
     * 用户登录
     * @param listener
     */
    @Override
    public void doLogin(HashMap hashMap,String url, final OnLoginFinishedListener listener) {
        RequestManager.getInstance(BaseAppliaction.getInstance()).requestAsyn(url, RequestManager.TYPE_POST_JSON, hashMap,
                new RequestManager.ReqCallBack<Object>() {
                    @Override
                    public void onReqSuccess(Object result) {
                        UserInfo userInfo=  GsonUtils.GsonToBean(result.toString(),UserInfo.class);
                        if(userInfo.getCode()==200){
                            //保存登录用户信息
                            SPUtils.put(BaseAppliaction.getInstance(),"userId",userInfo.getData().getUserId());
                            SPUtils.put(BaseAppliaction.getInstance(),"sessionId",userInfo.getData().getSessionId());
                            SPUtils.put(BaseAppliaction.getInstance(),"islogin",true);
                            listener.onSuccess();
                        }else{
                            listener.onError(userInfo.getMsg());
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        LogUtils.e("onReqFailed"+errorMsg.toString());
                    }
                });
    }
}
