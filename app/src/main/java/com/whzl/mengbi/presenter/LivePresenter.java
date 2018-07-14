package com.whzl.mengbi.presenter;

import java.util.HashMap;

public interface LivePresenter {
    void getLiveToken(HashMap hashMap);

    void getLiveGift();

    void getRoomInfo(int programId);

    void getAudienceAccount(int programId);

    void followHost(int userId, int programId);

    void getRoomUserInfo(int userId, int programId);

    void onDestory();

    void sendGift(int userId, int count, int goodId, int programId, int anchorId);
}
