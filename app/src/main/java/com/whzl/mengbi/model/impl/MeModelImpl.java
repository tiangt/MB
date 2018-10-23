package com.whzl.mengbi.model.impl;

import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.model.MeModel;
import com.whzl.mengbi.model.entity.GetNewTaskBean;
import com.whzl.mengbi.model.entity.UserInfo;
import com.whzl.mengbi.presenter.OnMeFinishedListener;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;
import com.whzl.mengbi.util.network.retrofit.ApiFactory;
import com.whzl.mengbi.util.network.retrofit.ApiObserver;
import com.whzl.mengbi.util.network.retrofit.ParamsUtils;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MeModelImpl implements MeModel {

    @Override
    public void doUserInfo(final OnMeFinishedListener listener) {
        HashMap paramsMap = new HashMap();
        long userId = Long.parseLong(SPUtils.get(BaseApplication.getInstance(), SpConfig.KEY_USER_ID, (long) 0).toString());
        paramsMap.put("userId", userId);
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.GET_USER_INFO, RequestManager.TYPE_POST_JSON, paramsMap,
                new RequestManager.ReqCallBack() {
                    @Override
                    public void onReqSuccess(Object result) {
                        UserInfo userInfo = GsonUtils.GsonToBean(result.toString(), UserInfo.class);
                        if (userInfo.getCode() == 200) {
                            listener.onSuccess(userInfo);
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        LogUtils.d("onReqFailed" + errorMsg);
                    }
                });
    }

    @Override
    public void doNewTask(OnMeFinishedListener listener) {
        HashMap paramsMap = new HashMap();
        long userId = Long.parseLong(SPUtils.get(BaseApplication.getInstance(), SpConfig.KEY_USER_ID, (long) 0).toString());
        paramsMap.put("userId", userId);
        ApiFactory.getInstance().getApi(Api.class)
                .getNewTask(ParamsUtils.getSignPramsMap(paramsMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<GetNewTaskBean>() {

                    @Override
                    public void onSuccess(GetNewTaskBean bean) {
                        listener.onNewTaskSuccess(bean);
                    }

                    @Override
                    public void onError(int code) {

                    }
                });
    }
}
