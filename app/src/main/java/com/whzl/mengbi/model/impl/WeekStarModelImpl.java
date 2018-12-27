package com.whzl.mengbi.model.impl;

import com.whzl.mengbi.model.WeekStarModel;
import com.whzl.mengbi.model.entity.WeekStarGiftInfo;
import com.whzl.mengbi.model.entity.WeekStarRankInfo;
import com.whzl.mengbi.presenter.OnWeekStarFinishedListener;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;

import java.util.HashMap;

/**
 * @author cliang
 * @date 2018.12.27
 */
public class WeekStarModelImpl implements WeekStarModel {

    @Override
    public void doGiftList(OnWeekStarFinishedListener listener) {
        HashMap hashMap = new HashMap();
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.WEEKSTAR_GIFT, RequestManager.TYPE_POST_JSON, hashMap,
                new RequestManager.ReqCallBack<Object>() {
                    @Override
                    public void onReqSuccess(Object result) {
                        WeekStarGiftInfo weekStarGiftInfo = GsonUtils.GsonToBean(result.toString(), WeekStarGiftInfo.class);
                        if (weekStarGiftInfo.getCode() == 200) {
                            listener.onGiftListSuccess(weekStarGiftInfo);
                        } else {
                            listener.onError(weekStarGiftInfo.getMsg());
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        LogUtils.d("onReqFailed" + errorMsg.toString());
                        listener.onError(errorMsg);
                    }
                });
    }

    @Override
    public void doRankList(String type, String preRound, OnWeekStarFinishedListener listener) {
        HashMap hashMap = new HashMap();
        hashMap.put("type", type);
        hashMap.put("preRound", preRound);
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.WEEKSTAR_RANK, RequestManager.TYPE_POST_JSON, hashMap,
                new RequestManager.ReqCallBack<Object>() {
                    @Override
                    public void onReqSuccess(Object result) {
                        WeekStarRankInfo weekStarRankInfo = GsonUtils.GsonToBean(result.toString(), WeekStarRankInfo.class);
                        if (weekStarRankInfo.getCode() == 200) {
                            listener.onRankListSuccess(weekStarRankInfo);
                        } else {
                            listener.onError(weekStarRankInfo.getMsg());
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        LogUtils.d("onReqFailed" + errorMsg.toString());
                        listener.onError(errorMsg);
                    }
                });
    }


}
