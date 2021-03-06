package com.whzl.mengbi.model;

import com.whzl.mengbi.presenter.OnLiveFinishedListener;

import java.util.HashMap;

import io.reactivex.Observable;

public interface LiveModel {
    void doLiveRoomToken(HashMap hashMap, OnLiveFinishedListener listener);

    void doLiveGift(OnLiveFinishedListener listener);

    void doRoomInfo(int programId, OnLiveFinishedListener listener);

    void doFollowHost(long userId, int programId, OnLiveFinishedListener listener);

    void doRoomUserInfo(long visitorId, int programId, OnLiveFinishedListener listener);

    void doSendGift(HashMap paramsMap, boolean useBag, OnLiveFinishedListener listener);

    void getRunWayList(HashMap paramsMap, OnLiveFinishedListener listener);

    void getProgramFirst(HashMap paramsMap, OnLiveFinishedListener listener);

    void activityList(HashMap paramsMap, OnLiveFinishedListener listener);

    void pkInfo(HashMap paramsMap, OnLiveFinishedListener listener);

    void getAudienceList(HashMap paramsMap, OnLiveFinishedListener listener);

    void getTotalGuard(HashMap paramsMap, OnLiveFinishedListener listener);

    void getRoomRankTotal(HashMap signPramsMap, OnLiveFinishedListener listener);

    void getDailyTaskState(HashMap signPramsMap, OnLiveFinishedListener listener);

    void getHeadlineRank(HashMap signPramsMap, OnLiveFinishedListener listener);

    void getBlackRoomTime(HashMap signPramsMap, OnLiveFinishedListener listener);

    void getUserSet(HashMap signPramsMap, OnLiveFinishedListener livePresenter);

    void getRedPackList(HashMap signPramsMap, OnLiveFinishedListener listener);

    void activityNative(HashMap signPramsMap, OnLiveFinishedListener listener);

    void anchorWish(HashMap signPramsMap, OnLiveFinishedListener listener);

    void getUpdownAnchor(HashMap signPramsMap, OnLiveFinishedListener listener);

    void getRoyalCarList(HashMap signPramsMap, OnLiveFinishedListener listener);

    void getRightBottomActivity(Observable merge, OnLiveFinishedListener listener);

    void getQualifying(HashMap signPramsMap, OnLiveFinishedListener listener);

    void roomGameRedpacket(HashMap signPramsMap, OnLiveFinishedListener listener);

    void pkGuess(HashMap signPramsMap, OnLiveFinishedListener listener);
}
