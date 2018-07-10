package com.whzl.mengbi.model;

import com.whzl.mengbi.presenter.OnLiveFinishedListener;

import java.util.HashMap;

public interface LiveModel {
    void doLiveRoomToken(HashMap hashMap, OnLiveFinishedListener listener);
    void doLiveGift(OnLiveFinishedListener listener);
    void doLiveFace(String fileName, OnLiveFinishedListener listener);
    void doRoomInfo(int programId, OnLiveFinishedListener listener);
    void doAudienceAccount(int programId, OnLiveFinishedListener listener);
    void doFellowHost(int userId, int programId, OnLiveFinishedListener listener);
}
