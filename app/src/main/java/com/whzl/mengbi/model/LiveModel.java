package com.whzl.mengbi.model;

import com.whzl.mengbi.presenter.OnLiveFinishedListener;

import java.util.HashMap;

public interface LiveModel {
    void doLiveRoomToken(HashMap hashMap, OnLiveFinishedListener listener);

    void doLiveGift(OnLiveFinishedListener listener);

    void doRoomInfo(int programId, OnLiveFinishedListener listener);

    void doFollowHost(long userId, int programId, OnLiveFinishedListener listener);

    void doRoomUserInfo(long visitorId,int programId, OnLiveFinishedListener listener);

    void doSendGift(HashMap paramsMap, OnLiveFinishedListener listener);

    void getRunWayList(HashMap paramsMap, OnLiveFinishedListener listener);

    void getProgramFirst(HashMap paramsMap, OnLiveFinishedListener listener);

    void activityList(HashMap paramsMap, OnLiveFinishedListener listener);

    void pkInfo(HashMap paramsMap, OnLiveFinishedListener listener);

    void activityGrand(HashMap paramsMap, OnLiveFinishedListener listener);

    void getAudienceList(HashMap paramsMap, OnLiveFinishedListener listener);

    void getTotalGuard(HashMap paramsMap, OnLiveFinishedListener listener);

    void getRoomRankTotal(HashMap signPramsMap, OnLiveFinishedListener listener);

    void getAnchorTask(HashMap signPramsMap, OnLiveFinishedListener listener);

    void getDailyTaskState(HashMap signPramsMap, OnLiveFinishedListener listener);

    void getHeadlineRank(HashMap signPramsMap, OnLiveFinishedListener listener);

    void getBlackRoomTime(HashMap signPramsMap, OnLiveFinishedListener listener);

    void getUserSet(HashMap signPramsMap, OnLiveFinishedListener livePresenter);

    void getRedPackTreasure(HashMap signPramsMap, OnLiveFinishedListener listener);

    void getRedPackList(HashMap signPramsMap, OnLiveFinishedListener listener);
}
