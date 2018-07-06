package com.whzl.mengbi.model.impl;

import com.whzl.mengbi.model.MeModel;
import com.whzl.mengbi.model.entity.UserInfo;
import com.whzl.mengbi.presenter.OnMeFinishedListener;
import com.whzl.mengbi.ui.common.BaseAppliaction;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;

import java.util.HashMap;

public class MeModelImpl implements MeModel{

    @Override
    public void doUserInfo(final OnMeFinishedListener listener) {
        HashMap paramsMap = new HashMap();
        int userId = (Integer) SPUtils.get(BaseAppliaction.getInstance(),"userId",0);
        paramsMap.put("userId",userId);
        RequestManager.getInstance(BaseAppliaction.getInstance()).requestAsyn(URLContentUtils.GET_USER_INFO,RequestManager.TYPE_POST_JSON,paramsMap,
                new RequestManager.ReqCallBack(){
                    @Override
                    public void onReqSuccess(Object result) {
                        UserInfo userInfo = GsonUtils.GsonToBean(result.toString(),UserInfo.class);
                        if(userInfo.getCode()==200){
                            listener.onSuccess(userInfo);
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        LogUtils.d("onReqFailed"+errorMsg);
                    }
                });
    }
}