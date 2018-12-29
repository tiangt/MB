package com.whzl.mengbi.presenter.impl;

import com.whzl.mengbi.model.LiveModel;
import com.whzl.mengbi.model.entity.ActivityGrandBean;
import com.whzl.mengbi.model.entity.AnchorTaskBean;
import com.whzl.mengbi.model.entity.AudienceListBean;
import com.whzl.mengbi.model.entity.BlackRoomTimeBean;
import com.whzl.mengbi.model.entity.GetActivityBean;
import com.whzl.mengbi.model.entity.GetDailyTaskStateBean;
import com.whzl.mengbi.model.entity.GiftInfo;
import com.whzl.mengbi.model.entity.GuardTotalBean;
import com.whzl.mengbi.model.entity.HeadlineRankBean;
import com.whzl.mengbi.model.entity.LiveRoomTokenInfo;
import com.whzl.mengbi.model.entity.PKResultBean;
import com.whzl.mengbi.model.entity.RoomInfoBean;
import com.whzl.mengbi.model.entity.RoomRankTotalBean;
import com.whzl.mengbi.model.entity.RoomUserInfo;
import com.whzl.mengbi.model.entity.RunWayListBean;
import com.whzl.mengbi.model.impl.LiveModelImpl;
import com.whzl.mengbi.presenter.LivePresenter;
import com.whzl.mengbi.presenter.OnLiveFinishedListener;
import com.whzl.mengbi.ui.view.LiveView;
import com.whzl.mengbi.util.network.retrofit.ParamsUtils;

import java.util.HashMap;

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
    public void sendGift(HashMap paramsMap) {
        liveModel.doSendGift(paramsMap, this);
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
    public void onSendGiftSuccess() {
        if (liveView != null) {
            liveView.onSendGiftSuccess();
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
    public void onActivityGrandSuccess(ActivityGrandBean bean) {
        if (liveView != null) {
            liveView.onActivityGrandSuccess(bean);
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
    public void onGetAnchorTaskSuccess(AnchorTaskBean dataBean) {
        if (liveView != null) {
            liveView.onGetAnchorTaskSuccess(dataBean);
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
        if(liveView != null){
            liveView.onGetHeadlineRankSuccess(dataBean);
        }
    }

    @Override
    public void onGetBlackRoomTime(BlackRoomTimeBean dataBean) {
        if(liveView != null){
            liveView.onGetBlackRoomTimeSuccess(dataBean);
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
    public void getActivityGrand(int programId, int anchorId) {
        HashMap map = new HashMap();
        map.put("programId", programId);
        map.put("anchorId", anchorId);
        HashMap signPramsMap = ParamsUtils.getSignPramsMap(map);
        liveModel.activityGrand(signPramsMap, this);
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

    public void getAnchorTask(int mAnchorId) {
        HashMap map = new HashMap();
        map.put("userId", mAnchorId);
        HashMap signPramsMap = ParamsUtils.getSignPramsMap(map);
        liveModel.getAnchorTask(signPramsMap, this);
    }
}
