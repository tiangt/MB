package com.whzl.mengbi.presenter;

import java.util.HashMap;

public interface LivePresenter {
    void getLiveToken(HashMap hashMap);

    void getLiveFace(String filename);

    void getLiveGift();

    void getRoomInfo(int programId);

    void getAudienceAccount(int programId);

    void onDestory();

}
