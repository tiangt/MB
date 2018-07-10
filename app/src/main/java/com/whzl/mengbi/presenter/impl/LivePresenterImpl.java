package com.whzl.mengbi.presenter.impl;

import com.whzl.mengbi.model.LiveModel;
import com.whzl.mengbi.model.entity.EmjoyInfo;
import com.whzl.mengbi.model.entity.GiftInfo;
import com.whzl.mengbi.model.entity.LiveRoomTokenInfo;
import com.whzl.mengbi.model.impl.LiveModelImpl;
import com.whzl.mengbi.presenter.LivePresenter;
import com.whzl.mengbi.presenter.OnLiveFinishedListener;
import com.whzl.mengbi.ui.view.LiveView;

import java.util.HashMap;

public class LivePresenterImpl implements LivePresenter,OnLiveFinishedListener {
    private LiveView liveView;
    private LiveModel liveModel;

    public LivePresenterImpl(LiveView liveView){
        this.liveView = liveView;
        liveModel = new LiveModelImpl();
    }

    @Override
    public void getLiveToken(HashMap hashMap) {
        liveModel.doLiveRoomToken(hashMap,this);
    }

    @Override
    public void getLiveFace(String filename) {
        liveModel.doLiveFace(filename,this);
    }

    @Override
    public void getLiveGift() {
        liveModel.doLiveGift(this);
    }

    @Override
    public void onDestroy() {
        liveView = null;
    }

    @Override
    public void onLiveTokenSuccess(LiveRoomTokenInfo liveRoomTokenInfo) {
        if(liveView != null){
            liveView.onLiveTokenSuccess(liveRoomTokenInfo);
        }
    }

    @Override
    public void onLiveGiftSuccess(GiftInfo giftInfo) {
        if(liveView != null){
            liveView.onLiveGiftSuccess(giftInfo);
        }
    }

    @Override
    public void onLiveFaceSuccess(EmjoyInfo emjoyInfo) {
        if(liveView != null){
            liveView.onLiveFaceSuccess(emjoyInfo);
        }
    }

    @Override
    public void onSuccess() {

    }

    @Override
    public void onEroor() {

    }
}
