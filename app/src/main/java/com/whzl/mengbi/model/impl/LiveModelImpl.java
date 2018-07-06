package com.whzl.mengbi.model.impl;

import com.whzl.mengbi.model.LiveModel;
import com.whzl.mengbi.model.entity.EmjoyInfo;
import com.whzl.mengbi.model.entity.GiftInfo;
import com.whzl.mengbi.model.entity.LiveRoomTokenInfo;
import com.whzl.mengbi.presenter.OnLiveFinishedListener;
import com.whzl.mengbi.ui.common.BaseAppliaction;
import com.whzl.mengbi.util.FileUtils;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;

import java.util.HashMap;

public class LiveModelImpl implements LiveModel {

    @Override
    public void doLiveRoomToken(HashMap hashMap, OnLiveFinishedListener listener) {
        RequestManager.getInstance(BaseAppliaction.getInstance()).requestAsyn(URLContentUtils.CHECK_ENTERR_ROOM, RequestManager.TYPE_POST_JSON, hashMap, new RequestManager.ReqCallBack<Object>() {
            @Override
            public void onReqSuccess(Object result) {
                LiveRoomTokenInfo liveRoomTokenInfo = GsonUtils.GsonToBean(result.toString(),LiveRoomTokenInfo.class);
                if(liveRoomTokenInfo.getCode()==200){
                    listener.onLiveTokenSuccess(liveRoomTokenInfo);
                }
            }

            @Override
            public void onReqFailed(String errorMsg) {

            }
        });
    }

    @Override
    public void doLiveGift(OnLiveFinishedListener listener) {
        HashMap parmarMap = new HashMap();
        RequestManager.getInstance(BaseAppliaction.getInstance()).requestAsyn(URLContentUtils.GIFT_LIST,RequestManager.TYPE_POST_JSON,parmarMap,
                new RequestManager.ReqCallBack<Object>() {

                    @Override
                    public void onReqSuccess(Object result) {
                        String strJson = result.toString();
                        GiftInfo giftInfo = GsonUtils.GsonToBean(strJson,GiftInfo.class);
                        if(giftInfo.getCode()==200){
                            listener.onLiveGiftSuccess(giftInfo);
                        }
                    }
                    @Override
                    public void onReqFailed(String errorMsg) {

                    }
                });
    }

    @Override
    public void doLiveFace(String fileName, OnLiveFinishedListener listener) {
        String strJson= FileUtils.getJson(fileName,BaseAppliaction.getInstance());
        EmjoyInfo emjoyInfo = GsonUtils.GsonToBean(strJson,EmjoyInfo.class);
        listener.onLiveFaceSuccess(emjoyInfo);
    }
}
