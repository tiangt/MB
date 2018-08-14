package com.whzl.mengbi.model.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.model.UserInfoModel;
import com.whzl.mengbi.presenter.OnUserInfoFInishedListener;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;
import com.whzl.mengbi.util.network.retrofit.ApiFactory;
import com.whzl.mengbi.util.network.retrofit.ApiObserver;
import com.whzl.mengbi.util.network.retrofit.ParamsUtils;

import java.io.File;
import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class UserInfoModelImpl implements UserInfoModel {

    @Override
    public void doUpdataPortrait(String userId, String filename, OnUserInfoFInishedListener listener) {
//        RequestManager.getInstance(BaseApplication.getInstance()).uploadAvatar(userId, new File(filename), URLContentUtils.MODIFY_AVATAR, new RequestManager.ReqCallBack() {
//            @Override
//            public void onReqSuccess(Object result) {
//                String strJson = result.toString();
//                JSONObject jsonObject = JSON.parseObject(strJson);
//                String code = jsonObject.get("code").toString();
//                if(code.equals("200")){
//                    String data = jsonObject.get("data").toString();
//                    JSONObject pathJsonObject = JSON.parseObject(data);
//                    String path = pathJsonObject.get("path").toString();
//                    listener.onPortraitSuccess(path);
//                }else{
//                    String msg = jsonObject.get("msg").toString();
//                    listener.onError(msg);
//                }
//            }
//
//            @Override
//            public void onReqFailed(String errorMsg) {
//
//            }
//        });
        HashMap paramsMap = new HashMap();
        paramsMap.put("userId", userId);
        File file = new File(filename);
        RequestBody requestFile = RequestBody.create(MediaType.parse("image/png"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData("avatar", file.getName(), requestFile);
        ApiFactory.getInstance().getApi(Api.class)
                .uploadFile(ParamsUtils.getMultiParamsMap(paramsMap), part)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<JsonElement>() {

                    @Override
                    public void onSuccess(JsonElement jsonElement) {
                        String jsonString = jsonElement.toString();
                        JSONObject jsonObject = JSONObject.parseObject(jsonString);
                        String path = jsonObject.getString("path");
                        listener.onPortraitSuccess(path);
                    }

                    @Override
                    public void onError(int code) {

                    }
                });
    }

    @Override
    public void doUpdataNickName(String userId, String nickname, OnUserInfoFInishedListener listener) {
        HashMap hashMap = new HashMap();
        hashMap.put("userId", userId);
        hashMap.put("nickname", nickname);
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.MODIFY_NICKNAME, RequestManager.TYPE_POST_JSON, hashMap, new RequestManager.ReqCallBack() {
            @Override
            public void onReqSuccess(Object result) {
                //SPUtils.put()
                String strJson = result.toString();
                JSONObject jsonObject = JSON.parseObject(strJson);
                String code = jsonObject.get("code").toString();
                if (code.equals("200")) {
                    String data = jsonObject.get("data").toString();
                    listener.onModifyNicknameSuc(nickname);
                    //listener.onModify
                } else {
                    String msg = jsonObject.get("msg").toString();
                    listener.onError(msg);
                }
            }

            @Override
            public void onReqFailed(String errorMsg) {
                //TODO: show toast
                LogUtils.d(errorMsg);
            }
        });
    }

    @Override
    public void doUpdataUserInfo(HashMap hashMap, OnUserInfoFInishedListener listener) {
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.MODIFY_USER_INFO, RequestManager.TYPE_POST_JSON, hashMap, new RequestManager.ReqCallBack() {
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
}
