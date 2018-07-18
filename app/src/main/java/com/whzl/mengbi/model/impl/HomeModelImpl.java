package com.whzl.mengbi.model.impl;

import com.alibaba.fastjson.JSON;
import com.whzl.mengbi.model.HomeModel;
import com.whzl.mengbi.model.entity.BannerInfo;
import com.whzl.mengbi.model.entity.LiveShowInfo;
import com.whzl.mengbi.model.entity.RecommendInfo;
import com.whzl.mengbi.presenter.OnHomeFinishedListener;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;

import java.util.HashMap;

/**
 * Class Note:首页
 */
public class HomeModelImpl implements HomeModel{

    @Override
    public void doBanner(final OnHomeFinishedListener listenter) {
        HashMap hashMap = new HashMap();
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.INDEX_ADV, RequestManager.TYPE_POST_JSON, hashMap,
                new RequestManager.ReqCallBack<Object>() {
                    @Override
                    public void onReqSuccess(Object result) {
                        BannerInfo bannerInfo = GsonUtils.GsonToBean(result.toString(), BannerInfo.class);
                        if(bannerInfo.getCode()==200){
                            listenter.onBannerSuccess(bannerInfo);
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        LogUtils.d("onReqFailed" + errorMsg.toString());
                    }
                });
    }

    @Override
    public void doRecommend(final OnHomeFinishedListener listenter) {
        HashMap hashMap = new HashMap();
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.RECOMMEND_ANCHOR, RequestManager.TYPE_POST_JSON, hashMap,
                new RequestManager.ReqCallBack<Object>() {
                    @Override
                    public void onReqSuccess(Object result) {
                        String jsonStr = result.toString();
                        RecommendInfo recommendInfo = JSON.parseObject(jsonStr,RecommendInfo.class);
                        if(recommendInfo.getCode()==200){
                            listenter.onRecommendSuccess(recommendInfo);
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        LogUtils.e("onReqFailed"+errorMsg);
                    }
                });
    }

    @Override
    public void doLiveShow(final OnHomeFinishedListener listenter) {
        HashMap liveMap = new HashMap();
        liveMap.put("page",1);
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.SHOW_ANCHOR, RequestManager.TYPE_POST_JSON, liveMap,
                new RequestManager.ReqCallBack<Object>() {
                    @Override
                    public void onReqSuccess(Object result) {
                        LiveShowInfo liveShowInfo = JSON.parseObject(result.toString(), LiveShowInfo.class);
                        listenter.onLiveShowSuccess(liveShowInfo);
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        LogUtils.e("onReqFailed"+errorMsg);
                    }
                });
    }

}
