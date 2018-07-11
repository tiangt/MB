package com.whzl.mengbi.model.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.whzl.mengbi.model.RechargeModel;
import com.whzl.mengbi.model.entity.RechargeInfo;
import com.whzl.mengbi.presenter.OnRechargeFinishedListener;
import com.whzl.mengbi.ui.common.BaseAppliaction;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;

import java.util.HashMap;

public class RechargeModelImpl implements RechargeModel {

    @Override
    public void doRechargeChannel(HashMap hashMap,OnRechargeFinishedListener listener) {
        RequestManager.getInstance(BaseAppliaction.getInstance()).requestAsyn(URLContentUtils.RECHARGE_GET_CHANNEL, RequestManager.TYPE_POST_JSON, hashMap,
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
        RequestManager.getInstance(BaseAppliaction.getInstance()).requestAsyn(URLContentUtils.RECHARGE_ORDER, RequestManager.TYPE_POST_JSON, hashMap,
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
}