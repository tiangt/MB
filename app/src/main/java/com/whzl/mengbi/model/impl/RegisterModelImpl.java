package com.whzl.mengbi.model.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.whzl.mengbi.model.RegisterModel;
import com.whzl.mengbi.model.entity.RegisterInfo;
import com.whzl.mengbi.presenter.OnRegisterFinishedListener;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;

import java.util.HashMap;

public class RegisterModelImpl implements RegisterModel {

    @Override
    public void doRegexCode(String mobile, final OnRegisterFinishedListener listener) {
        HashMap paramsMapMobile = new HashMap();
        paramsMapMobile.put("mobile",mobile);
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.SEND_CODE, RequestManager.TYPE_POST_JSON, paramsMapMobile,
                new RequestManager.ReqCallBack<Object>() {
                    @Override
                    public void onReqSuccess(Object result) {
                        JSONObject jsonObject = JSON.parseObject(result.toString());
                        String code = jsonObject.get("code").toString();
                        if(!code.equals(200)){
                            String msg = jsonObject.get("msg").toString();
                            listener.onRegexCodeSuccess(code,msg);
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        LogUtils.d("errorMsg"+errorMsg.toString());
                    }
                });
    }

    @Override
    public void doRegister(String username, String password, String code,final OnRegisterFinishedListener listener) {
        HashMap paramsMap = new HashMap();
        paramsMap.put("platform","ANDROID");
        paramsMap.put("password",password);
        paramsMap.put("code",code);
        paramsMap.put("username",username);

        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.REGISTER, RequestManager.TYPE_POST_JSON, paramsMap,
                new RequestManager.ReqCallBack<Object>() {
                    @Override
                    public void onReqSuccess(Object result) {
                        RegisterInfo registerInfo =  GsonUtils.GsonToBean(result.toString(),RegisterInfo.class);
                        if (registerInfo.getCode()==200){
                            listener.onSuccess(registerInfo);
                        }else{
                            listener.onError(registerInfo.getMsg());
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        LogUtils.d("errorMsg"+errorMsg.toString());
                    }
                });
    }
}
