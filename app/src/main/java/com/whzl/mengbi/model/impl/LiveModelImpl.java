package com.whzl.mengbi.model.impl;

import com.google.gson.JsonElement;
import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.model.LiveModel;
import com.whzl.mengbi.model.entity.ActivityGrandBean;
import com.whzl.mengbi.model.entity.AnchorTaskBean;
import com.whzl.mengbi.model.entity.AnchorWishBean;
import com.whzl.mengbi.model.entity.ApiResult;
import com.whzl.mengbi.model.entity.AudienceListBean;
import com.whzl.mengbi.model.entity.BlackRoomTimeBean;
import com.whzl.mengbi.model.entity.GetActivityBean;
import com.whzl.mengbi.model.entity.GetDailyTaskStateBean;
import com.whzl.mengbi.model.entity.GetUserSetBean;
import com.whzl.mengbi.model.entity.GiftInfo;
import com.whzl.mengbi.model.entity.GuardTotalBean;
import com.whzl.mengbi.model.entity.HeadlineRankBean;
import com.whzl.mengbi.model.entity.LiveRoomTokenInfo;
import com.whzl.mengbi.model.entity.ModifyNameCardBean;
import com.whzl.mengbi.model.entity.PKResultBean;
import com.whzl.mengbi.model.entity.ResponseInfo;
import com.whzl.mengbi.model.entity.RoomInfoBean;
import com.whzl.mengbi.model.entity.RoomRankTotalBean;
import com.whzl.mengbi.model.entity.RoomRedpackList;
import com.whzl.mengbi.model.entity.RoomUserInfo;
import com.whzl.mengbi.model.entity.RunWayListBean;
import com.whzl.mengbi.model.entity.UpdownAnchorBean;
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
import java.util.concurrent.TimeUnit;

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
    public void doFollowHost(long userId, int programId, OnLiveFinishedListener listener) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("programId", programId + "");
        paramsMap.put("userId", userId + "");
        ApiFactory.getInstance().getApi(Api.class)
                .addSub(ParamsUtils.getSignPramsMap(paramsMap))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<JsonElement>() {
                    @Override
                    public void onSuccess(JsonElement jsonElement) {
                        listener.onFellowHostSuccess();
                    }
                });
//        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.FELLOW_HOST, RequestManager.TYPE_POST_JSON, paramsMap, new RequestManager.ReqCallBack<Object>() {
//            @Override
//            public void onReqSuccess(Object result) {
//                ResponseInfo responseInfo = GsonUtils.GsonToBean(result.toString(), ResponseInfo.class);
//                if (responseInfo.getCode() == 200) {
//                    listener.onFellowHostSuccess();
//                } else {
//                    listener.onError(responseInfo.getMsg());
//                }
//            }
//
//            @Override
//            public void onReqFailed(String errorMsg) {
//                listener.onError(errorMsg);
//            }
//        });
    }

    @Override
    public void doRoomUserInfo(long visitorId, int programId, OnLiveFinishedListener listener) {
        HashMap<String, String> paramsMap = new HashMap<>();
        paramsMap.put("programId", programId + "");
        paramsMap.put("userId", visitorId + "");
//        paramsMap.put("visitorId", visitorId + "");
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
    public void doSendGift(HashMap paramsMap, boolean useBag, OnLiveFinishedListener listener) {
        RequestManager.getInstance(BaseApplication.getInstance()).requestAsyn(URLContentUtils.SEND_GIFT, RequestManager.TYPE_POST_JSON, paramsMap, new RequestManager.ReqCallBack<Object>() {
            @Override
            public void onReqSuccess(Object result) {
                ResponseInfo responseInfo = GsonUtils.GsonToBean(result.toString(), ResponseInfo.class);
                if (responseInfo.getCode() == 200) {
                    listener.onSendGiftSuccess(useBag);
                } else if (responseInfo.getCode() == -1211) {
                    listener.onSendGiftNoMoney();
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
                .subscribe(new ApiObserver<PKResultBean>() {
                    @Override
                    public void onSuccess(PKResultBean jsonElement) {
                        listener.onPkInfoSuccess(jsonElement);
                    }

                    @Override
                    public void onError(ApiResult<PKResultBean> body) {

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
                        listener.onRightBottomActivityError();
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

    @Override
    public void getTotalGuard(HashMap paramsMap, OnLiveFinishedListener listener) {
        ApiFactory.getInstance().getApi(Api.class)
                .getTotalGuard(paramsMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<GuardTotalBean.DataBean>() {
                    @Override
                    public void onSuccess(GuardTotalBean.DataBean dataBean) {
                        if (dataBean != null) {
                            listener.onGetTotalGuardSuccess(dataBean);
                        }
                    }

                    @Override
                    public void onError(int code) {

                    }
                });
    }

    @Override
    public void getRoomRankTotal(HashMap signPramsMap, OnLiveFinishedListener listener) {
        ApiFactory.getInstance().getApi(Api.class)
                .getRoomRankTotal(signPramsMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<RoomRankTotalBean>() {
                    @Override
                    public void onSuccess(RoomRankTotalBean dataBean) {
                        if (dataBean != null) {
                            listener.onGetRoomRankTotalSuccess(dataBean);
                        }
                    }

                    @Override
                    public void onError(int code) {

                    }
                });
    }

    @Override
    public void getAnchorTask(HashMap signPramsMap, OnLiveFinishedListener listener) {
        ApiFactory.getInstance().getApi(Api.class)
                .getAnchorTask(signPramsMap)
                .delay(200, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<AnchorTaskBean>() {
                    @Override
                    public void onSuccess(AnchorTaskBean dataBean) {
                        if (dataBean != null) {
                            listener.onGetAnchorTaskSuccess(dataBean);
                        }
                    }

                    @Override
                    public void onError(ApiResult<AnchorTaskBean> body) {
                        listener.onRightBottomActivityError();
                    }
                });
    }

    @Override
    public void getDailyTaskState(HashMap signPramsMap, OnLiveFinishedListener listener) {
        ApiFactory.getInstance().getApi(Api.class)
                .getDailyTaskState(signPramsMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<GetDailyTaskStateBean>() {
                    @Override
                    public void onSuccess(GetDailyTaskStateBean dataBean) {
                        if (dataBean != null) {
                            listener.onGetDailyTaskStateSuccuss(dataBean);
                        }
                    }

                    @Override
                    public void onError(ApiResult<GetDailyTaskStateBean> body) {
                    }
                });
    }

    @Override
    public void getHeadlineRank(HashMap signPramsMap, OnLiveFinishedListener listener) {
        ApiFactory.getInstance().getApi(Api.class)
                .getHeadlineRank(signPramsMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<HeadlineRankBean>() {
                    @Override
                    public void onSuccess(HeadlineRankBean dataBean) {
                        if (dataBean != null) {
                            listener.onGetHeadlineRankSuccess(dataBean);
                        }
                    }

                    @Override
                    public void onError(ApiResult<HeadlineRankBean> body) {
                    }
                });
    }

    @Override
    public void getBlackRoomTime(HashMap signPramsMap, OnLiveFinishedListener listener) {
        ApiFactory.getInstance().getApi(Api.class)
                .getRoomTime(signPramsMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<BlackRoomTimeBean>() {
                    @Override
                    public void onSuccess(BlackRoomTimeBean dataBean) {
                        if (dataBean != null) {
                            listener.onGetBlackRoomTime(dataBean);
                        }
                    }

                    @Override
                    public void onError(ApiResult<BlackRoomTimeBean> body) {
                    }
                });
    }

    @Override
    public void getUserSet(HashMap signPramsMap, OnLiveFinishedListener livePresenter) {
        ApiFactory.getInstance().getApi(Api.class)
                .getUserSet(signPramsMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<GetUserSetBean>() {
                    @Override
                    public void onSuccess(GetUserSetBean dataBean) {
                        if (dataBean != null) {
                            livePresenter.onGetUserSet(dataBean);
                        }
                    }

                    @Override
                    public void onError(ApiResult<GetUserSetBean> body) {
                    }
                });
    }

    @Override
    public void getRedPackList(HashMap signPramsMap, OnLiveFinishedListener listener) {
        ApiFactory.getInstance().getApi(Api.class)
                .getRoomRedpacketList(signPramsMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<RoomRedpackList>() {
                    @Override
                    public void onSuccess(RoomRedpackList dataBean) {
                        if (dataBean != null) {
                            listener.onGetRoomRedList(dataBean);
                        }
                    }

                    @Override
                    public void onError(ApiResult<RoomRedpackList> body) {
                    }
                });
    }

    @Override
    public void activityNative(HashMap signPramsMap, OnLiveFinishedListener listener) {
        ApiFactory.getInstance().getApi(Api.class)
                .activityNative(signPramsMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<GetActivityBean>() {


                    @Override
                    public void onSuccess(GetActivityBean jsonElement) {
                        listener.onActivityNativeSuccess(jsonElement);
                    }

                    @Override
                    public void onError(int code) {
                    }
                });
    }

    @Override
    public void anchorWish(HashMap signPramsMap, OnLiveFinishedListener listener) {
        ApiFactory.getInstance().getApi(Api.class)
                .anchorWishGift(signPramsMap)
                .debounce(1500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<AnchorWishBean>() {


                    @Override
                    public void onSuccess(AnchorWishBean jsonElement) {
                        listener.onAnchorWishSuccess(jsonElement);
                    }

                    @Override
                    public void onError(ApiResult<AnchorWishBean> body) {
                        listener.onRightBottomActivityError();

                    }
                });
    }

    @Override
    public void getUpdownAnchor(HashMap signPramsMap, OnLiveFinishedListener listener) {
        ApiFactory.getInstance().getApi(Api.class)
                .updownAnchor(signPramsMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ApiObserver<UpdownAnchorBean>() {


                    @Override
                    public void onSuccess(UpdownAnchorBean jsonElement) {
                        listener.onUpdownAnchors(jsonElement);
                    }

                    @Override
                    public void onError(ApiResult<UpdownAnchorBean> body) {

                    }
                });
    }

}
