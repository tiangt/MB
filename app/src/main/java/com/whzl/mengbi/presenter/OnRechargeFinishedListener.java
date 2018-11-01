package com.whzl.mengbi.presenter;

import com.whzl.mengbi.model.entity.RebateBean;
import com.whzl.mengbi.model.entity.RechargeInfo;
import com.whzl.mengbi.model.entity.UserInfo;

public interface OnRechargeFinishedListener {
    void onChannelSuccess(RechargeInfo rechargeInfo);
    void onOrderSuccess(int orderId,String token);
    void onGetUserInfoSuccess(UserInfo userInfo);
    void onGetCoupon(RebateBean rebateBean);
}
