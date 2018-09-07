package com.whzl.mengbi.model;

import com.whzl.mengbi.presenter.OnLiveFinishedListener;

import java.util.HashMap;

public interface LiveModel {
    void doLiveRoomToken(HashMap hashMap, OnLiveFinishedListener listener);
    void doLiveGift(OnLiveFinishedListener listener);
    void doRoomInfo(int programId, OnLiveFinishedListener listener);
    void doAudienceAccount(int programId, OnLiveFinishedListener listener);

    void doFollowHost(long userId, int programId, OnLiveFinishedListener listener);

    void doRoomUserInfo(long userId, int programId, OnLiveFinishedListener listener);

    void doSendGift(HashMap paramsMap, OnLiveFinishedListener listener);

    void getRunWayList(HashMap paramsMap, OnLiveFinishedListener listener);

    void getGuardList(HashMap paramsMap, OnLiveFinishedListener listener);

    void getProgramFirst(HashMap paramsMap, OnLiveFinishedListener listener);

    void getTreasureBox(HashMap paramsMap, OnLiveFinishedListener listener);

    void treceiveTreasure(HashMap paramsMap, OnLiveFinishedListener listener);
}
