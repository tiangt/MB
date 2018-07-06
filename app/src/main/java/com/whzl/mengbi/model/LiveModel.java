package com.whzl.mengbi.model;

import com.whzl.mengbi.presenter.OnLiveFinishedListener;

import java.util.HashMap;

public interface LiveModel {
    void doLiveRoomToken(HashMap hashMap, OnLiveFinishedListener listener);
    void doLiveGift(OnLiveFinishedListener listener);
    void doLiveFace(String fileName, OnLiveFinishedListener listener);

}