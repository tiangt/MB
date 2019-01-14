package com.whzl.mengbi.model.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.whzl.mengbi.model.BindingModel;
import com.whzl.mengbi.presenter.OnBindingFinishedListener;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;

import java.util.HashMap;

/**
 * @author cliang
 * @date 2019.1.14
 */
public class BindingModelImpl implements BindingModel {

    @Override
    public void doRegexCode(String mobile, OnBindingFinishedListener listener) {
        HashMap paramsMapMobile = new HashMap();
        paramsMapMobile.put("mobile", mobile);
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.SEND_CODE, RequestManager.TYPE_POST_JSON, paramsMapMobile,
                new RequestManager.ReqCallBack<Object>() {
                    @Override
                    public void onReqSuccess(Object result) {
                        JSONObject jsonObject = JSON.parseObject(result.toString());
                        String code = jsonObject.get("code").toString();
                        if (!code.equals(200)) {
                            String msg = jsonObject.get("msg").toString();
                            listener.onRegexCodeSuccess(code, msg);
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        LogUtils.d("errorMsg" + errorMsg.toString());
                    }
                });
    }
}
