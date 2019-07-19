package com.whzl.mengbi.presenter;

import java.util.HashMap;

public interface LivePresenter {
    void getLiveToken(HashMap hashMap);

    void getLiveGift();

    void getRoomInfo(int programId);


    void followHost(long userId, int programId);

    void getRoomUserInfo(long visitorId,  int programId);

    void onDestory();

    void sendGift(HashMap paramsMap, boolean useBag);

    void getRunWayList(HashMap paramsMap);

    void getProgramFirst(int programId);

    void getActivityList();

    void getPkInfo(int programId);

    void getAudienceList(int programId);

    void getGuardTotal(int programId);

    void getRoomRankTotal(int mProgramId, String type);

    void getDailyTaskState(long mUserId);

    void getHeadlineRank(int anchorId, String preRound);

    void getBlackRoomTime(int mAnchorId);

    void getUserSet(long mUserId);

    void getActivityNative(int mProgramId, int mAnchorId);

    void getAnchorWish(int mAnchorId);

    void getUpdownAnchor();

    void getRoyalCarList();

    void getRightBottomActivity(int mProgramId, int mAnchorId);
}
