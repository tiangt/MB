package com.whzl.mengbi.model.impl;

import com.google.gson.JsonElement;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.model.GuardListBean;
import com.whzl.mengbi.model.LiveModel;
import com.whzl.mengbi.model.entity.ActivityGrandBean;
import com.whzl.mengbi.model.entity.ApiResult;
import com.whzl.mengbi.model.entity.AudienceCountBean;
import com.whzl.mengbi.model.entity.AudienceListBean;
import com.whzl.mengbi.model.entity.GetActivityBean;
import com.whzl.mengbi.model.entity.GiftInfo;
import com.whzl.mengbi.model.entity.LiveRoomTokenInfo;
import com.whzl.mengbi.model.entity.PkInfoBean;
import com.whzl.mengbi.model.entity.ResponseInfo;
import com.whzl.mengbi.model.entity.RoomInfoBean;
import com.whzl.mengbi.model.entity.RoomUserInfo;
import com.whzl.mengbi.model.entity.RunWayListBean;
import com.whzl.mengbi.model.entity.TreasureBoxStatusBean;
import com.whzl.mengbi.presenter.OnLiveFinishedListener;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;
import com.whzl.mengbi.util.network.retrofit.ApiFactory;
import com.whzl.mengbi.util.network.retrofit.ApiObserver;
import com.whzl.mengbi.util.network.retrofit.ParamsUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class LiveModelImpl implements LiveModel {

    @Override
    public void doLiveRoomToken(HashMap hashMap, OnLiveFinishedListener listener) {
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.CHECK_ENTERR_ROOM, RequestManager.TYPE_POST_JSON, hashMap, new RequestManager.ReqCallBack<Object>() {
            @Override
            public void onReqSuccess(Object result) {
                LiveRoomTokenInfo liveRoomTokenInfo = GsonUtils.GsonToBean(result.toString(), LiveRoomTokenInfo.class);
                if (liveRoomTokenInfo.getCode() == 200) {
                    listener.onLiveTokenSuccess(liveRoomTokenInfo);
                } else {
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
        HashMap paramsMap = new HashMap();
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.GIFT_LIST, RequestManager.TYPE_POST_JSON, paramsMap,
                new RequestManager.ReqCallBack<Object>() {

                    @Override
                    public void onReqSuccess(Object result) {
                        String strJson = result.toString();
                        GiftInfo giftInfo = GsonUtils.GsonToBean(strJson, GiftInfo.class);
                        if (giftInfo.getCode() == 200) {
                            listener.onLiveGiftSuccess(giftInfo);
                        } else {
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
    public void doRoomInfo(int programId, OnLiveFinishedListener listener) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("programId", programId + "");
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.ROOM_INFO, RequestManager.TYPE_POST_JSON, paramsMap, new RequestManager.ReqCallBack<Object>() {
            @Override
            public void onReqSuccess(Object result) {
                RoomInfoBean roomInfoBean = GsonUtils.GsonToBean(result.toString(), RoomInfoBean.class);
                if (roomInfoBean.getCode() == 200) {
                    listener.onRoomInfoSuccess(roomInfoBean);
                } else {
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
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.AUDIENCE_COUNT, RequestManager.TYPE_POST_JSON, paramsMap, new RequestManager.ReqCallBack<Object>() {
            @Override
            public void onReqSuccess(Object result) {
                AudienceCountBean audienceCountBean = GsonUtils.GsonToBean(result.toString(), AudienceCountBean.class);
                if (audienceCountBean.getCode() == 200) {
                    if (audienceCountBean.getData() != null) {
                        listener.onAudienceSuccess(audienceCountBean.getData().getRoomUserNum());
                    }
                } else {
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
    public void doFollowHost(long userId, int programId, OnLiveFinishedListener listener) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("programId", programId + "");
        paramsMap.put("userId", userId + "");
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.FELLOW_HOST, RequestManager.TYPE_POST_JSON, paramsMap, new RequestManager.ReqCallBack<Object>() {
            @Override
            public void onReqSuccess(Object result) {
                ResponseInfo responseInfo = GsonUtils.GsonToBean(result.toString(), ResponseInfo.class);
                if (responseInfo.getCode() == 200) {
                    listener.onFellowHostSuccess();
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

    @Override
    public void doRoomUserInfo(long userId, int programId, OnLiveFinishedListener listener) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("programId", programId + "");
        paramsMap.put("userId", userId + "");
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.ROOM_USER_INFO, RequestManager.TYPE_POST_JSON, paramsMap, new RequestManager.ReqCallBack<Object>() {
            @Override
            public void onReqSuccess(Object result) {
                RoomUserInfo roomUserInfo = GsonUtils.GsonToBean(result.toString(), RoomUserInfo.class);
                if (roomUserInfo.getCode() == 200) {
                    if (roomUserInfo.getData() != null) {
                        listener.onGetRoomUserInfoSuccess(roomUserInfo.getData());
                    }
                } else {
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
    public void doSendGift(HashMap paramsMap, OnLiveFinishedListener listener) {
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.SEND_GIFT, RequestManager.TYPE_POST_JSON, paramsMap, new RequestManager.ReqCallBack<Object>() {
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

    @Override
    public void getRunWayList(HashMap paramsMap, OnLiveFinishedListener listener) {
        ApiFactory.getInstance().getApi(Api.class)
                .getRunWayList(paramsMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<RunWayListBean>() {
                    @Override
                    public void onSuccess(RunWayListBean runWayListBean) {
                        listener.onGetRunWayListSuccess(runWayListBean);
                    }

                    @Override
                    public void onError(int code) {
                    }
                });

    }

    @Override
    public void getGuardList(HashMap paramsMap, OnLiveFinishedListener listener) {
        ApiFactory.getInstance().getApi(Api.class)
                .getGuardList(paramsMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<GuardListBean>() {
                    @Override
                    public void onSuccess(GuardListBean guardListBean) {
                        listener.onGetGuardListSuccess(guardListBean);
                    }

                    @Override
                    public void onError(int code) {
                    }
                });

    }

    @Override
    public void getProgramFirst(HashMap paramsMap, OnLiveFinishedListener listener) {
        ApiFactory.getInstance().getApi(Api.class)
                .getProgramFirst(paramsMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<JsonElement>() {

                    @Override
                    public void onSuccess(JsonElement jsonElement) {
                        String jsonStr = jsonElement.toString();
                        JSONObject jsonObject;
                        try {
                            jsonObject = new JSONObject(jsonStr);
                            long userId = jsonObject.getLong("userId");
                            listener.onGetProgramFirstSuccess(userId);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(int code) {

                    }
                });

    }

    @Override
    public void getTreasureBox(HashMap paramsMap, OnLiveFinishedListener listener){
        ApiFactory.getInstance().getApi(Api.class)
                .getTreasureBoxStatus(paramsMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<TreasureBoxStatusBean>() {


                    @Override
                    public void onSuccess(TreasureBoxStatusBean treasureBoxStatusBean) {
                        listener.onTreasureStatusSuccess(treasureBoxStatusBean);
                    }

                    @Override
                    public void onError(int code) {

                    }
                });
    }

    @Override
    public void treceiveTreasure(HashMap paramsMap, OnLiveFinishedListener listener){
        ApiFactory.getInstance().getApi(Api.class)
                .receiveTreasure(paramsMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<JsonElement>() {


                    @Override
                    public void onSuccess(JsonElement jsonElement) {
                        listener.onReceiveTreasureSuccess();
                    }

                    @Override
                    public void onError(int code) {

                    }
                });
    }

    @Override
    public void activityList(HashMap paramsMap, OnLiveFinishedListener listener) {
        ApiFactory.getInstance().getApi(Api.class)
                .activityList(paramsMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<GetActivityBean>() {


                    @Override
                    public void onSuccess(GetActivityBean jsonElement) {
                        listener.onActivityListSuccess(jsonElement);
                    }

                    @Override
                    public void onError(int code) {

                    }
                });
    }

    @Override
    public void pkInfo(HashMap paramsMap, OnLiveFinishedListener listener) {
        ApiFactory.getInstance().getApi(Api.class)
                .pkInfo(paramsMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<PkInfoBean>() {


                    @Override
                    public void onSuccess(PkInfoBean jsonElement) {
                        listener.onPkInfoSuccess(jsonElement);
                    }

                    @Override
                    public void onError(ApiResult<PkInfoBean> body) {

                    }
                });
    }

    @Override
    public void activityGrand(HashMap paramsMap, OnLiveFinishedListener listener) {
        ApiFactory.getInstance().getApi(Api.class)
                .activityGrand(paramsMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<ActivityGrandBean>() {


                    @Override
                    public void onSuccess(ActivityGrandBean jsonElement) {
                        listener.onActivityGrandSuccess(jsonElement);
                    }

                    @Override
                    public void onError(int code) {

                    }
                });
    }

    @Override
    public void getAudienceList(HashMap paramsMap, OnLiveFinishedListener listener) {
        ApiFactory.getInstance().getApi(Api.class)
                .getAudienceList(paramsMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<AudienceListBean.DataBean>() {
                    @Override
                    public void onSuccess(AudienceListBean.DataBean dataBean) {
                        if (dataBean != null) {
                            listener.onGetAudienceListSuccess(dataBean);
                        }
                    }

                    @Override
                    public void onError(int code) {

                    }
                });
    }
}
