package com.whzl.mengbi.model;

import com.whzl.mengbi.presenter.OnLiveFinishedListener;

import java.util.HashMap;

public interface LiveModel {
    void doLiveRoomToken(HashMap hashMap, OnLiveFinishedListener listener);
    void doLiveGift(OnLiveFinishedListener listener);
    void doLiveFace(String fileName, OnLiveFinishedListener listener);
    void doRoomInfo(int programId, OnLiveFinishedListener listener);
    void doAudienceAccount(int programId, OnLiveFinishedListener listener);

    void doFollowHost(long userId, int programId, OnLiveFinishedListener listener);

    void doRoomUserInfo(long userId, int programId, OnLiveFinishedListener listener);

    void doSendGift(long userId, int count, int goodsId, int programId, int targetId, OnLiveFinishedListener listener);
}
