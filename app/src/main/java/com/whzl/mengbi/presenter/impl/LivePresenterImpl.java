package com.whzl.mengbi.presenter.impl;

import com.google.gson.JsonElement;
import com.whzl.mengbi.model.GuardListBean;
import com.whzl.mengbi.model.LiveModel;
import com.whzl.mengbi.model.entity.GetActivityBean;
import com.whzl.mengbi.model.entity.GiftInfo;
import com.whzl.mengbi.model.entity.LiveRoomTokenInfo;
import com.whzl.mengbi.model.entity.PkInfoBean;
import com.whzl.mengbi.model.entity.RoomInfoBean;
import com.whzl.mengbi.model.entity.RoomUserInfo;
import com.whzl.mengbi.model.entity.RunWayListBean;
import com.whzl.mengbi.model.entity.TreasureBoxStatusBean;
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
    public void getAudienceAccount(int programId) {
        liveModel.doAudienceAccount(programId, this);

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
    public void onAudienceSuccess(long audienceAccount) {
        if (liveView != null) {
            liveView.onAudienceSuccess(audienceAccount);
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
    public void onGetGuardListSuccess(GuardListBean guardListBean) {
        if (liveView != null) {
            liveView.getGuardListSuccess(guardListBean);
        }
    }

    @Override
    public void onGetProgramFirstSuccess(long userId) {
        if (liveView != null) {
            liveView.onGetProgramFirstSuccess(userId);
        }
    }

    @Override
    public void onTreasureStatusSuccess(TreasureBoxStatusBean treasureBoxStatusBean) {
        if (liveView != null) {
            liveView.onTreasureSuccess(treasureBoxStatusBean);
        }
    }

    @Override
    public void onReceiveTreasureSuccess() {
        if (liveView != null) {
            liveView.onReceiveTreasureSuccess();
        }
    }

    @Override
    public void onActivityListSuccess(GetActivityBean bean) {
        if (liveView != null) {
            liveView.onActivityListSuccess(bean);
        }
    }

    @Override
    public void onPkInfoSuccess(PkInfoBean bean) {
        if (liveView != null) {
            liveView.onPkInfoSuccess(bean);
        }
    }

    @Override
    public void followHost(long userId, int mProgramId) {
        liveModel.doFollowHost(userId, mProgramId, this);
    }

    @Override
    public void getRoomUserInfo(long userId, int programId) {
        if (userId != 0) {
            liveModel.doRoomUserInfo(userId, programId, this);
        }
    }

    @Override
    public void getGuardList(int programId) {
        HashMap map = new HashMap();
        map.put("programId", programId);
        HashMap signPramsMap = ParamsUtils.getSignPramsMap(map);
        liveModel.getGuardList(signPramsMap, this);
    }

    @Override
    public void getProgramFirst(int programId) {
        HashMap map = new HashMap();
        map.put("programId", programId);
        HashMap signPramsMap = ParamsUtils.getSignPramsMap(map);
        liveModel.getProgramFirst(signPramsMap, this);
    }

    @Override
    public void getTreasureBoxStatus(long userId) {
        HashMap map = new HashMap();
        map.put("userId", userId);
        HashMap signPramsMap = ParamsUtils.getSignPramsMap(map);
        liveModel.getTreasureBox(signPramsMap, this);
    }

    @Override
    public void receiveTreasure(long userId) {
        HashMap map = new HashMap();
        map.put("userId", userId);
        HashMap signPramsMap = ParamsUtils.getSignPramsMap(map);
        liveModel.treceiveTreasure(signPramsMap, this);
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
}
