package com.whzl.mengbi.model.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.whzl.mengbi.model.UserInfoModel;
import com.whzl.mengbi.presenter.OnUserInfoFInishedListener;
import com.whzl.mengbi.ui.common.BaseAppliaction;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;

import java.util.HashMap;

public class UserInfoModelImpl implements UserInfoModel{

    @Override
    public void doUpdataPortrait(String userId,String filename, OnUserInfoFInishedListener listener) {
        HashMap hashMap = new HashMap();
        hashMap.put("userId",userId);
        hashMap.put("avatar",filename);
        RequestManager.getInstance(BaseAppliaction.getInstance()).requestAsyn(URLContentUtils.MODIFY_AVATAR,RequestManager.TYPE_POST_JSON,hashMap, new RequestManager.ReqCallBack() {
            @Override
            public void onReqSuccess(Object result) {
                String strJson = result.toString();
                JSONObject jsonObject = JSON.parseObject(strJson);
                String code = jsonObject.get("code").toString();
                if(code.equals("200")){
                    String data = jsonObject.get("data").toString();
                    listener.onPortraitSuccess(data);
                }else{
                    String msg = jsonObject.get("msg").toString();
                    listener.onError(msg);
                }
            }

            @Override
            public void onReqFailed(String errorMsg) {
                LogUtils.d(errorMsg);
            }
        });
    }

    @Override
    public void doUpdataNickName(String userId,String nickname, OnUserInfoFInishedListener listener) {
        HashMap hashMap = new HashMap();
        hashMap.put("userId",userId);
        hashMap.put("nickname",nickname);
        RequestManager.getInstance(BaseAppliaction.getInstance()).requestAsyn(URLContentUtils.MODIFY_NICKNAME,RequestManager.TYPE_POST_JSON,hashMap, new RequestManager.ReqCallBack() {
            @Override
            public void onReqSuccess(Object result) {
                String strJson = result.toString();
                JSONObject jsonObject = JSON.parseObject(strJson);
                String code = jsonObject.get("code").toString();
                if(code.equals("200")){
                    String data = jsonObject.get("data").toString();
                    listener.onSuccess(data);
                }else{
                    String msg = jsonObject.get("msg").toString();
                    listener.onError(msg);
                }
            }

            @Override
            public void onReqFailed(String errorMsg) {
                LogUtils.d(errorMsg);
            }
        });
    }

    @Override
    public void doUpdataSex(String userId, String sex, OnUserInfoFInishedListener listener) {
        HashMap hashMap = new HashMap();
        hashMap.put("userId", userId);
        hashMap.put("sex", sex);
        RequestManager.getInstance(BaseAppliaction.getInstance()).requestAsyn(URLContentUtils.MODIFY_USER_INFO, RequestManager.TYPE_POST_JSON, hashMap, new RequestManager.ReqCallBack() {
            @Override
            public void onReqSuccess(Object result) {
                String strJson = result.toString();
                JSONObject jsonObject = JSON.parseObject(strJson);
                String code = jsonObject.get("code").toString();
                if (code.equals("200")) {
                    String data = jsonObject.get("data").toString();
                    listener.onSuccess(data);
                } else {
                    String msg = jsonObject.get("msg").toString();
                    listener.onError(msg);
                }
            }

            @Override
            public void onReqFailed(String errorMsg) {
                LogUtils.d(errorMsg);
            }
        });
    }

    @Override
    public void doUpdataAddress(String userId, String province, String city, OnUserInfoFInishedListener listener) {
        HashMap hashMap = new HashMap();
        hashMap.put("userId", userId);
        hashMap.put("province", province);
        hashMap.put("city", city);
        RequestManager.getInstance(BaseAppliaction.getInstance()).requestAsyn(URLContentUtils.MODIFY_USER_INFO, RequestManager.TYPE_POST_JSON, hashMap, new RequestManager.ReqCallBack() {
            @Override
            public void onReqSuccess(Object result) {
                String strJson = result.toString();
                JSONObject jsonObject = JSON.parseObject(strJson);
                String code = jsonObject.get("code").toString();
                if (code.equals("200")) {
                    String data = jsonObject.get("data").toString();
                    listener.onSuccess(data);
                } else if (code.equals("503")) {
                    listener.onError("保存失败");
                } else {
                    listener.onError("输入有非法字符");
                }
            }

            @Override
            public void onReqFailed(String errorMsg) {
                LogUtils.d(errorMsg);
            }
        });
    }

    @Override
    public void doUpdataIntroduce(String userId, String introduce, OnUserInfoFInishedListener listener) {
        HashMap hashMap = new HashMap();
        hashMap.put("userId", userId);
        hashMap.put("introduce", introduce);
        RequestManager.getInstance(BaseAppliaction.getInstance()).requestAsyn(URLContentUtils.MODIFY_USER_INFO, RequestManager.TYPE_POST_JSON, hashMap, new RequestManager.ReqCallBack() {
            @Override
            public void onReqSuccess(Object result) {
                String strJson = result.toString();
                JSONObject jsonObject = JSON.parseObject(strJson);
                String code = jsonObject.get("code").toString();
                if (code.equals("200")) {
                    String data = jsonObject.get("data").toString();
                    listener.onSuccess(data);
                } else {
                    String msg = jsonObject.get("msg").toString();
                    listener.onError(msg);
                }
            }

            @Override
            public void onReqFailed(String errorMsg) {
                LogUtils.d(errorMsg);
            }
        });
    }

    @Override
    public void doUpdataBirthday(String userId, String birthday, OnUserInfoFInishedListener listener) {
        HashMap hashMap = new HashMap();
        hashMap.put("userId", userId);
        hashMap.put("birthday", birthday);
        RequestManager.getInstance(BaseAppliaction.getInstance()).requestAsyn(URLContentUtils.MODIFY_USER_INFO,RequestManager.TYPE_POST_JSON,hashMap, new RequestManager.ReqCallBack() {
            @Override
            public void onReqSuccess(Object result) {
                String strJson = result.toString();
                JSONObject jsonObject = JSON.parseObject(strJson);
                String code = jsonObject.get("code").toString();
                if(code.equals("200")){
                    String data = jsonObject.get("data").toString();
                    listener.onSuccess(data);
                }else{
                    String msg = jsonObject.get("msg").toString();
                    listener.onError(msg);
                }
            }

            @Override
            public void onReqFailed(String errorMsg) {
                LogUtils.d(errorMsg);
            }
        });
    }
}
