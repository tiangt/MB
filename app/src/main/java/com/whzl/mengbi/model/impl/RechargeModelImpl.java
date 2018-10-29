package com.whzl.mengbi.model.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.model.RechargeModel;
import com.whzl.mengbi.model.entity.RebateBean;
import com.whzl.mengbi.model.entity.RechargeInfo;
import com.whzl.mengbi.model.entity.UserInfo;
import com.whzl.mengbi.presenter.OnRechargeFinishedListener;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;
import com.whzl.mengbi.util.network.retrofit.ApiFactory;
import com.whzl.mengbi.util.network.retrofit.ApiObserver;
import com.whzl.mengbi.util.network.retrofit.ParamsUtils;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class RechargeModelImpl implements RechargeModel {

    @Override
    public void doUserInfo(long userId, final OnRechargeFinishedListener listener) {
        HashMap paramsMap = new HashMap();
        paramsMap.put("userId",userId);
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.GET_USER_INFO,RequestManager.TYPE_POST_JSON,paramsMap,
                new RequestManager.ReqCallBack(){
                    @Override
                    public void onReqSuccess(Object result) {
                        UserInfo userInfo = GsonUtils.GsonToBean(result.toString(),UserInfo.class);
                        if (userInfo.getCode() == 200) {
                            listener.onGetUserInfoSuccess(userInfo);
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        LogUtils.d("onReqFailed"+errorMsg);
                    }
                });
    }

    @Override
    public void doRechargeChannel(HashMap hashMap,OnRechargeFinishedListener listener) {
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.RECHARGE_GET_CHANNEL, RequestManager.TYPE_POST_JSON, hashMap,
                new RequestManager.ReqCallBack<Object>() {
                    @Override
                    public void onReqSuccess(Object result) {
                        RechargeInfo rechargeInfo = JSON.parseObject(result.toString(),RechargeInfo.class);
                        if(rechargeInfo.getCode() == RequestManager.RESPONSE_CODE){
                            listener.onChannelSuccess(rechargeInfo);
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        LogUtils.d(errorMsg);
                    }
                });
    }

    @Override
    public void doRechargeOrder(HashMap hashMap, OnRechargeFinishedListener listener) {
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.RECHARGE_ORDER, RequestManager.TYPE_POST_JSON, hashMap,
                new RequestManager.ReqCallBack<Object>() {
                    @Override
                    public void onReqSuccess(Object result) {
                        String resultStr = result.toString();
                        JSONObject jsonObject = JSON.parseObject(resultStr);
                        int code = Integer.parseInt(jsonObject.get("code").toString());
                        JSONObject dataJson = jsonObject.getJSONObject("data");
                        if(code == RequestManager.RESPONSE_CODE){
                            int orderId = Integer.parseInt(dataJson.get("orderId").toString());
                            String token = dataJson.get("token").toString();
                            listener.onOrderSuccess(orderId,token);
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        LogUtils.d(errorMsg);
                    }
                });
    }

    @Override
    public void doCoupon(long usetId, OnRechargeFinishedListener listener) {
        HashMap params = new HashMap();
        params.put("userId", usetId);
        Map signPramsMap = ParamsUtils.getSignPramsMap(params);
        ApiFactory.getInstance().getApi(Api.class)
                .findCoupon(signPramsMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<RebateBean>() {

                    @Override
                    public void onSuccess(RebateBean rebateBean) {
                        listener.onGetCoupon(rebateBean);
                    }

                    @Override
                    public void onError(int code) {

                    }
                });
    }
}
