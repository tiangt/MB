package com.whzl.mengbi.presenter;

import java.util.HashMap;

public interface LivePresenter {
    void getLiveToken(HashMap hashMap);

    void getLiveFace(String filename);

    void getLiveGift();

    void onDestory();

}
