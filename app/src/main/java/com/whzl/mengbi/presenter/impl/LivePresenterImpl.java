package com.whzl.mengbi.presenter.impl;

import com.whzl.mengbi.api.Api;
import com.whzl.mengbi.model.LiveModel;
import com.whzl.mengbi.model.entity.AnchorWishBean;
import com.whzl.mengbi.model.entity.AudienceListBean;
import com.whzl.mengbi.model.entity.BlackRoomTimeBean;
import com.whzl.mengbi.model.entity.GetActivityBean;
import com.whzl.mengbi.model.entity.GetDailyTaskStateBean;
import com.whzl.mengbi.model.entity.GetUserSetBean;
import com.whzl.mengbi.model.entity.GiftInfo;
import com.whzl.mengbi.model.entity.GuardTotalBean;
import com.whzl.mengbi.model.entity.HeadlineRankBean;
import com.whzl.mengbi.model.entity.LiveRoomTokenInfo;
import com.whzl.mengbi.model.entity.PKResultBean;
import com.whzl.mengbi.model.entity.PkGuessBean;
import com.whzl.mengbi.model.entity.PkQualifyingBean;
import com.whzl.mengbi.model.entity.RoomInfoBean;
import com.whzl.mengbi.model.entity.RoomRankTotalBean;
import com.whzl.mengbi.model.entity.RoomRedpackList;
import com.whzl.mengbi.model.entity.RoomRedpacketBean;
import com.whzl.mengbi.model.entity.RoomUserInfo;
import com.whzl.mengbi.model.entity.RoyalCarListBean;
import com.whzl.mengbi.model.entity.RunWayListBean;
import com.whzl.mengbi.model.entity.UpdownAnchorBean;
import com.whzl.mengbi.model.impl.LiveModelImpl;
import com.whzl.mengbi.presenter.LivePresenter;
import com.whzl.mengbi.presenter.OnLiveFinishedListener;
import com.whzl.mengbi.ui.view.LiveView;
import com.whzl.mengbi.util.network.retrofit.ApiFactory;
import com.whzl.mengbi.util.network.retrofit.ParamsUtils;

import java.util.HashMap;

import io.reactivex.Observable;

public class LivePresenterImpl implements LivePresenter, OnLiveFinishedListener {
    private LiveView liveView;
    private LiveModel liveModel;

    public LivePresenterImpl(LiveView liveView) {
        this.liveView = liveView;
        liveModel = new LiveModelImpl();
    }

    @Override
    public void getLiveToken(HashMap hashMap) {
        liveModel.doLiveRoomToken(hashMap, this);
    }

    @Override
    public void getLiveGift() {
        liveModel.doLiveGift(this);
    }

    @Override
    public void getRoomInfo(int programId) {
        liveModel.doRoomInfo(programId, this);
    }


    @Override
    public void onDestory() {
        liveView = null;
    }

    @Override
    public void sendGift(HashMap paramsMap, boolean useBag) {
        liveModel.doSendGift(paramsMap, useBag, this);
    }

    @Override
    public void getRunWayList(HashMap paramsMap) {
        liveModel.getRunWayList(paramsMap, this);
    }

    @Override
    public void onLiveTokenSuccess(LiveRoomTokenInfo liveRoomTokenInfo) {
        if (liveView != null) {
            liveView.onLiveTokenSuccess(liveRoomTokenInfo);
        }
    }

    @Override
    public void onLiveGiftSuccess(GiftInfo giftInfo) {
        if (liveView != null) {
            liveView.onLiveGiftSuccess(giftInfo);
        }
    }

    @Override
    public void onRoomInfoSuccess(RoomInfoBean roomInfoBean) {
        if (liveView != null) {
            liveView.onRoomInfoSuccess(roomInfoBean);
        }
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onFellowHostSuccess() {
        if (liveView != null) {
            liveView.onFollowHostSuccess();
        }
    }

    @Override
    public void onError(String msg) {
        if (liveView != null) {
            liveView.onError(msg);
        }
    }

    @Override
    public void onGetRoomUserInfoSuccess(RoomUserInfo.DataBean data) {
        if (liveView != null) {
            liveView.onGetRoomUserInFoSuccess(data);
        }
    }

    @Override
    public void onSendGiftSuccess(boolean useBag) {
        if (liveView != null) {
            liveView.onSendGiftSuccess(useBag);
        }
    }

    @Override
    public void onSendGiftNoMoney() {
        if (liveView != null) {
            liveView.onSendGiftNoMoney();
        }
    }

    @Override
    public void onUpdownAnchors(UpdownAnchorBean jsonElement) {
        if (liveView != null) {
            liveView.onUpdownAnchors(jsonElement);
        }
    }


    @Override
    public void onRoyalCarListSuccess(RoyalCarListBean jsonElement) {
        if (liveView != null) {
            liveView.onGetRoyalCarListSuccess(jsonElement);
        }
    }

    @Override
    public void onRightBottomActivitySuccuss(Object o) {
        if (liveView != null) {
            liveView.onRightBottomActivitySuccess(o);
        }
    }

    @Override
    public void onQualifyingSuccess(PkQualifyingBean anchorInfoBean) {
        if (liveView != null) {
            liveView.onQualifyingSuccess(anchorInfoBean);
        }
    }

    @Override
    public void onRoomGameRedpacketSuccess(RoomRedpacketBean jsonElement) {
        if (liveView != null) {
            liveView.onRoomGameRedpacketSuccess(jsonElement);
        }
    }

    @Override
    public void onPkGuessSuccess(PkGuessBean pkGuessBean) {
        if (liveView != null) {
            liveView.onPkGuessSuccess(pkGuessBean);
        }
    }

    @Override
    public void onGetRunWayListSuccess(RunWayListBean runWayListBean) {
        if (liveView != null) {
            liveView.onGetRunWayListSuccess(runWayListBean);
        }
    }


    @Override
    public void onGetProgramFirstSuccess(long userId) {
        if (liveView != null) {
            liveView.onGetProgramFirstSuccess(userId);
        }
    }


    @Override
    public void onActivityListSuccess(GetActivityBean bean) {
        if (liveView != null) {
            liveView.onActivityListSuccess(bean);
        }
    }

    @Override
    public void onPkInfoSuccess(PKResultBean bean) {
        if (liveView != null) {
            liveView.onPkInfoSuccess(bean);
        }
    }


    @Override
    public void onGetAudienceListSuccess(AudienceListBean.DataBean bean) {
        if (liveView != null) {
            liveView.onGetAudienceListSuccess(bean);
        }
    }

    @Override
    public void onGetTotalGuardSuccess(GuardTotalBean.DataBean bean) {
        if (liveView != null) {
            liveView.onGetTotalGuardSuccess(bean);
        }
    }

    @Override
    public void onGetRoomRankTotalSuccess(RoomRankTotalBean bean) {
        if (liveView != null) {
            liveView.onGetRoomRankTotalSuccess(bean);
        }
    }

    @Override
    public void onGetDailyTaskStateSuccuss(GetDailyTaskStateBean dailyTaskStateBean) {
        if (liveView != null) {
            liveView.onGetDailyTaskStateSuccess(dailyTaskStateBean);
        }
    }

    @Override
    public void onGetHeadlineRankSuccess(HeadlineRankBean dataBean) {
        if (liveView != null) {
            liveView.onGetHeadlineRankSuccess(dataBean);
        }
    }

    @Override
    public void onGetBlackRoomTime(BlackRoomTimeBean dataBean) {
        if (liveView != null) {
            liveView.onGetBlackRoomTimeSuccess(dataBean);
        }
    }

    @Override
    public void onGetUserSet(GetUserSetBean dataBean) {
        if (liveView != null) {
            liveView.onGetUsetSetSuccesd(dataBean);
        }
    }

    @Override
    public void onGetRoomRedList(RoomRedpackList dataBean) {
        if (liveView != null) {
            liveView.onGetRoomRedListSuccess(dataBean);
        }
    }

    @Override
    public void onActivityNativeSuccess(GetActivityBean bean) {
        if (liveView != null) {
            liveView.onActivityNativeSuccess(bean);
        }
    }

    @Override
    public void onAnchorWishSuccess(AnchorWishBean jsonElement) {
        if (liveView != null) {
            liveView.onAnchorWishSuccess(jsonElement);
        }
    }


    @Override
    public void followHost(long userId, int mProgramId) {
        liveModel.doFollowHost(userId, mProgramId, this);
    }

    @Override
    public void getRoomUserInfo(long visitorId, int programId) {
        if (visitorId != 0) {
            liveModel.doRoomUserInfo(visitorId, programId, this);
        }
    }

    @Override
    public void getProgramFirst(int programId) {
        HashMap map = new HashMap();
        map.put("programId", programId);
        HashMap signPramsMap = ParamsUtils.getSignPramsMap(map);
        liveModel.getProgramFirst(signPramsMap, this);
    }

    @Override
    public void getActivityList() {
        HashMap map = new HashMap();
        HashMap signPramsMap = ParamsUtils.getSignPramsMap(map);
        liveModel.activityList(signPramsMap, this);
    }

    @Override
    public void getPkInfo(int programId) {
        HashMap map = new HashMap();
        map.put("programId", programId);
        HashMap signPramsMap = ParamsUtils.getSignPramsMap(map);
        liveModel.pkInfo(signPramsMap, this);
    }

    @Override
    public void getAudienceList(int programId) {
        HashMap map = new HashMap();
        map.put("programId", programId);
        HashMap signPramsMap = ParamsUtils.getSignPramsMap(map);
        liveModel.getAudienceList(signPramsMap, this);
    }

    @Override
    public void getGuardTotal(int programId) {
        HashMap map = new HashMap();
        map.put("programId", programId);
        HashMap signPramsMap = ParamsUtils.getSignPramsMap(map);
        liveModel.getTotalGuard(signPramsMap, this);
    }


    @Override
    public void getRoomRankTotal(int mProgramId, String type) {
        HashMap map = new HashMap();
        map.put("programId", mProgramId);
        map.put("type", type);
        HashMap signPramsMap = ParamsUtils.getSignPramsMap(map);
        liveModel.getRoomRankTotal(signPramsMap, this);
    }

    @Override
    public void getDailyTaskState(long mUserId) {
        HashMap map = new HashMap();
        map.put("userId", mUserId);
        HashMap signPramsMap = ParamsUtils.getSignPramsMap(map);
        liveModel.getDailyTaskState(signPramsMap, this);
    }

    @Override
    public void getHeadlineRank(int anchorId, String preRound) {
        HashMap map = new HashMap();
        map.put("userId", anchorId);
        map.put("preRound", preRound);
        HashMap signPramsMap = ParamsUtils.getSignPramsMap(map);
        liveModel.getHeadlineRank(signPramsMap, this);
    }

    @Override
    public void getBlackRoomTime(int mAnchorId) {
        HashMap map = new HashMap();
        map.put("userId", mAnchorId);
        HashMap signPramsMap = ParamsUtils.getSignPramsMap(map);
        liveModel.getBlackRoomTime(signPramsMap, this);
    }


    @Override
    public void getUserSet(long mUserId) {
        HashMap map = new HashMap();
        map.put("userId", mUserId);
        HashMap signPramsMap = ParamsUtils.getSignPramsMap(map);
        liveModel.getUserSet(signPramsMap, this);
    }

    @Override
    public void getActivityNative(int mProgramId, int mAnchorId) {
        HashMap map = new HashMap();
        map.put("programId", mProgramId);
        map.put("anchorId", mAnchorId);
        HashMap signPramsMap = ParamsUtils.getSignPramsMap(map);
        liveModel.activityNative(signPramsMap, this);
    }

    @Override
    public void getAnchorWish(int mAnchorId) {
        HashMap map = new HashMap();
        map.put("userId", mAnchorId);
        HashMap signPramsMap = ParamsUtils.getSignPramsMap(map);
        liveModel.anchorWish(signPramsMap, this);
    }

    @Override
    public void getUpdownAnchor() {
        HashMap map = new HashMap();
        HashMap signPramsMap = ParamsUtils.getSignPramsMap(map);
        liveModel.getUpdownAnchor(signPramsMap, this);
    }

    @Override
    public void getRoyalCarList() {
        HashMap map = new HashMap();
        HashMap signPramsMap = ParamsUtils.getSignPramsMap(map);
        liveModel.getRoyalCarList(signPramsMap, this);
    }

    @Override
    public void getRightBottomActivity(int mProgramId, int mAnchorId) {
        HashMap map = new HashMap();
        map.put("userId", mAnchorId);
        HashMap signPramsMap = ParamsUtils.getSignPramsMap(map);
        Observable observable = ApiFactory.getInstance().getApi(Api.class)
                .anchorWishGift(signPramsMap);

        HashMap map1 = new HashMap();
        map1.put("programId", mProgramId);
        map1.put("anchorId", mAnchorId);
        HashMap signPramsMap1 = ParamsUtils.getSignPramsMap(map1);
        Observable observable1 = ApiFactory.getInstance().getApi(Api.class)
                .activityGrand(signPramsMap1);

        HashMap map2 = new HashMap();
        map2.put("userId", mAnchorId);
        HashMap signPramsMap2 = ParamsUtils.getSignPramsMap(map2);
        Observable observable2 = ApiFactory.getInstance().getApi(Api.class)
                .getAnchorTask(signPramsMap2);

        Observable merge = Observable.merge(observable, observable1, observable2);
        liveModel.getRightBottomActivity(merge, this);
    }

    @Override
    public void getQualifying(int mAnchorId) {
        HashMap map = new HashMap();
        map.put("userId", mAnchorId);
        HashMap signPramsMap = ParamsUtils.getSignPramsMap(map);
        liveModel.getQualifying(signPramsMap, this);
    }

    @Override
    public void roomGameRedpacket(long mUserId, int mProgramId) {
        HashMap map = new HashMap(16);
        map.put("userId", mUserId);
        map.put("programId", mProgramId);
        HashMap signPramsMap = ParamsUtils.getSignPramsMap(map);
        liveModel.roomGameRedpacket(signPramsMap, this);
    }

    @Override
    public void pkGuess(int mAnchorId) {
        HashMap map = new HashMap();
        map.put("userId", mAnchorId);
        HashMap signPramsMap = ParamsUtils.getSignPramsMap(map);
        liveModel.pkGuess(signPramsMap, this);
    }

    public void getRedPackList(int mProgramId, long mUserId) {
        HashMap map = new HashMap();
        map.put("programId", mProgramId);
        map.put("userId", mUserId);
        HashMap signPramsMap = ParamsUtils.getSignPramsMap(map);
        liveModel.getRedPackList(signPramsMap, this);
    }
}
