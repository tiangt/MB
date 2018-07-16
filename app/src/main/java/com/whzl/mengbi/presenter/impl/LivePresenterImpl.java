package com.whzl.mengbi.presenter.impl;

import com.whzl.mengbi.model.LiveModel;
import com.whzl.mengbi.model.entity.EmjoyInfo;
import com.whzl.mengbi.model.entity.GiftInfo;
import com.whzl.mengbi.model.entity.LiveRoomTokenInfo;
import com.whzl.mengbi.model.entity.RoomInfoBean;
import com.whzl.mengbi.model.entity.RoomUserInfo;
import com.whzl.mengbi.model.impl.LiveModelImpl;
import com.whzl.mengbi.presenter.LivePresenter;
import com.whzl.mengbi.presenter.OnLiveFinishedListener;
import com.whzl.mengbi.ui.view.LiveView;

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
    public void sendGift(int userId, int count, int goodId, int programId, int anchorId) {
        liveModel.doSendGift(userId, count, goodId, programId, anchorId, this);
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
    public void onLiveFaceSuccess(EmjoyInfo emjoyInfo) {
        if (liveView != null) {
            liveView.onLiveFaceSuccess(emjoyInfo);
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
    public void followHost(int userId, int mProgramId) {
        liveModel.doFollowHost(userId, mProgramId, this);
    }

    @Override
    public void getRoomUserInfo(int userId, int programId) {
        if (userId != 0) {
            liveModel.doRoomUserInfo(userId, programId, this);
        }
    }
}
