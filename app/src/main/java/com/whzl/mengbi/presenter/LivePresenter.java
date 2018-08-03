package com.whzl.mengbi.presenter;

import java.util.HashMap;

public interface LivePresenter {
    void getLiveToken(HashMap hashMap);

    void getLiveGift();

    void getRoomInfo(int programId);

    void getAudienceAccount(int programId);

    void followHost(long userId, int programId);

    void getRoomUserInfo(long userId, int programId);

    void onDestory();

    void sendGift(HashMap paramsMap);

    void getRunWayList(HashMap paramsMap);
}
