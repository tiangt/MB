package com.whzl.mengbi.presenter;

import com.whzl.mengbi.model.entity.RechargeInfo;

public interface OnRechargeFinishedListener {
    void onChannelSuccess(RechargeInfo rechargeInfo);
    void onOrderSuccess(int orderId,String token);
}
