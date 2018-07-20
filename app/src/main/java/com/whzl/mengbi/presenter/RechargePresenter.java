package com.whzl.mengbi.presenter;

import java.util.HashMap;

public interface RechargePresenter {
    void getChannelInfo(HashMap hashMap);
    void getOrderInfo(HashMap hashMap);

    void getUserInfo(int userId);

    void onDestroy();
}
