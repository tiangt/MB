package com.whzl.mengbi.ui.view;

import com.whzl.mengbi.model.entity.RechargeInfo;
import com.whzl.mengbi.model.entity.UserInfo;

public interface RechargeView {
    void onGetChannelInfoSuccess(RechargeInfo rechargeInfo);
    void onGetPayOrderSuccess(int orderId, String token);
    void onGetUserInfoSuccess(UserInfo userInfo);
}
