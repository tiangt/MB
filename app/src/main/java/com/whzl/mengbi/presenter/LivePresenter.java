package com.whzl.mengbi.presenter;

import java.util.HashMap;

public interface LivePresenter {
    void getLiveToken(HashMap hashMap);

    void getLiveGift();

    void getRoomInfo(int programId);


    void followHost(long userId, int programId);

    void getRoomUserInfo(long visitorId, int userId, int programId);

    void onDestory();

    void sendGift(HashMap paramsMap);

    void getRunWayList(HashMap paramsMap);

    void getProgramFirst(int programId);

    void getActivityList();

    void getPkInfo(int programId);

    void getActivityGrand(int programId, int anchorId);

    void getAudienceList(int programId);

    void getGuardTotal(int programId);

    void getRoomRankTotal(int mProgramId, String type);

}
