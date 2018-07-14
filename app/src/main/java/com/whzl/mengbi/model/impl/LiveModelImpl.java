package com.whzl.mengbi.model.impl;

import com.whzl.mengbi.model.LiveModel;
import com.whzl.mengbi.model.entity.AudienceCountBean;
import com.whzl.mengbi.model.entity.EmjoyInfo;
import com.whzl.mengbi.model.entity.GiftInfo;
import com.whzl.mengbi.model.entity.LiveRoomTokenInfo;
import com.whzl.mengbi.model.entity.ResponseInfo;
import com.whzl.mengbi.model.entity.RoomInfoBean;
import com.whzl.mengbi.model.entity.RoomUserInfo;
import com.whzl.mengbi.presenter.OnLiveFinishedListener;
import com.whzl.mengbi.ui.common.BaseAppliaction;
import com.whzl.mengbi.util.FileUtils;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.SPUtils;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;

import java.util.HashMap;

public class LiveModelImpl implements LiveModel {

    @Override
    public void doLiveRoomToken(HashMap hashMap, OnLiveFinishedListener listener) {
        RequestManager.getInstance(BaseAppliaction.getInstance()).requestAsyn(URLContentUtils.CHECK_ENTERR_ROOM, RequestManager.TYPE_POST_JSON, hashMap, new RequestManager.ReqCallBack<Object>() {
            @Override
            public void onReqSuccess(Object result) {
                LiveRoomTokenInfo liveRoomTokenInfo = GsonUtils.GsonToBean(result.toString(), LiveRoomTokenInfo.class);
                if (liveRoomTokenInfo.getCode() == 200) {
                    listener.onLiveTokenSuccess(liveRoomTokenInfo);
                }else {
                    listener.onError(liveRoomTokenInfo.getMsg());
                }
            }

            @Override
            public void onReqFailed(String errorMsg) {
                listener.onError(errorMsg);
            }
        });
    }

    @Override
    public void doLiveGift(OnLiveFinishedListener listener) {
        HashMap parmarMap = new HashMap();
        RequestManager.getInstance(BaseAppliaction.getInstance()).requestAsyn(URLContentUtils.GIFT_LIST, RequestManager.TYPE_POST_JSON, parmarMap,
                new RequestManager.ReqCallBack<Object>() {

                    @Override
                    public void onReqSuccess(Object result) {
                        String strJson = result.toString();
                        GiftInfo giftInfo = GsonUtils.GsonToBean(strJson, GiftInfo.class);
                        if (giftInfo.getCode() == 200) {
                            listener.onLiveGiftSuccess(giftInfo);
                        }else {
                            listener.onError(giftInfo.getMsg());
                        }
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        LogUtils.d(errorMsg);
                        listener.onError(errorMsg);
                    }
                });
    }

    @Override
    public void doLiveFace(String fileName, OnLiveFinishedListener listener) {
        String strJson = FileUtils.getJson(fileName, BaseAppliaction.getInstance());
        EmjoyInfo emjoyInfo = GsonUtils.GsonToBean(strJson, EmjoyInfo.class);
        listener.onLiveFaceSuccess(emjoyInfo);
    }


    @Override
    public void doRoomInfo(int programId, OnLiveFinishedListener listener) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("programId", programId + "");
        RequestManager.getInstance(BaseAppliaction.getInstance()).requestAsyn(URLContentUtils.ROOM_INFO, RequestManager.TYPE_POST_JSON, paramsMap, new RequestManager.ReqCallBack<Object>() {
            @Override
            public void onReqSuccess(Object result) {
                RoomInfoBean roomInfoBean = GsonUtils.GsonToBean(result.toString(), RoomInfoBean.class);
                if (roomInfoBean.getCode() == 200) {
                    listener.onRoomInfoSuccess(roomInfoBean);
                }else {
                    listener.onError(roomInfoBean.getMsg());
                }
            }

            @Override
            public void onReqFailed(String errorMsg) {
                listener.onError(errorMsg);
            }
        });
    }

    @Override
    public void doAudienceAccount(int programId, OnLiveFinishedListener listener) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("programId", programId + "");
        RequestManager.getInstance(BaseAppliaction.getInstance()).requestAsyn(URLContentUtils.AUDIENCE_COUNT, RequestManager.TYPE_POST_JSON, paramsMap, new RequestManager.ReqCallBack<Object>() {
            @Override
            public void onReqSuccess(Object result) {
                AudienceCountBean audienceCountBean = GsonUtils.GsonToBean(result.toString(), AudienceCountBean.class);
                if (audienceCountBean.getCode() == 200) {
                    if (audienceCountBean.getData() != null) {
                        listener.onAudienceSuccess(audienceCountBean.getData().getRoomUserNum());
                    }
                }else {
                    listener.onError(audienceCountBean.getMsg());
                }
            }

            @Override
            public void onReqFailed(String errorMsg) {
                listener.onError(errorMsg);
            }
        });
    }

    @Override
    public void doFollowHost(int userId, int programId, OnLiveFinishedListener listener) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("programId", programId + "");
        paramsMap.put("userId", userId + "");
        RequestManager.getInstance(BaseAppliaction.getInstance()).requestAsyn(URLContentUtils.FELLOW_HOST, RequestManager.TYPE_POST_JSON, paramsMap, new RequestManager.ReqCallBack<Object>() {
            @Override
            public void onReqSuccess(Object result) {
                ResponseInfo responseInfo = GsonUtils.GsonToBean(result.toString(), ResponseInfo.class);
                if (responseInfo.getCode() == 200) {
                    listener.onFellowHostSuccess();
                }else {
                    listener.onError(responseInfo.getMsg());
                }
            }

            @Override
            public void onReqFailed(String errorMsg) {
                listener.onError(errorMsg);
            }
        });
    }

    @Override
    public void doRoomUserInfo(int userId, int programId, OnLiveFinishedListener listener) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("programId", programId + "");
        paramsMap.put("userId", userId + "");
        RequestManager.getInstance(BaseAppliaction.getInstance()).requestAsyn(URLContentUtils.ROOM_USER_INFO, RequestManager.TYPE_POST_JSON, paramsMap, new RequestManager.ReqCallBack<Object>() {
            @Override
            public void onReqSuccess(Object result) {
                RoomUserInfo roomUserInfo = GsonUtils.GsonToBean(result.toString(), RoomUserInfo.class);
                if (roomUserInfo.getCode() == 200) {
                    if (roomUserInfo.getData() != null) {
                        listener.onGetRoomUserInfoSuccess(roomUserInfo.getData());
                    }
                }else {
                    listener.onError(roomUserInfo.getMsg());
                }
            }

            @Override
            public void onReqFailed(String errorMsg) {
                listener.onError(errorMsg);
            }
        });
    }

    @Override
    public void doSendGift(int userId, int count, int goodsId, int programId, int targetId, OnLiveFinishedListener listener) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("roomId", programId + "");
        paramsMap.put("programId", programId + "");
        paramsMap.put("targetId", targetId + "");
        paramsMap.put("goodsId", goodsId + "");
        paramsMap.put("count", count + "");
        paramsMap.put("userId", userId + "");
        paramsMap.put("useBag", false + "");
        RequestManager.getInstance(BaseAppliaction.getInstance()).requestAsyn(URLContentUtils.SEND_GIFT, RequestManager.TYPE_POST_JSON, paramsMap, new RequestManager.ReqCallBack<Object>() {
            @Override
            public void onReqSuccess(Object result) {
                ResponseInfo responseInfo = GsonUtils.GsonToBean(result.toString(), ResponseInfo.class);
                if (responseInfo.getCode() == 200) {
                    listener.onSendGiftSuccess();
                } else {
                    listener.onError(responseInfo.getMsg());
                }
            }

            @Override
            public void onReqFailed(String errorMsg) {
                listener.onError(errorMsg);
            }
        });
    }
}
