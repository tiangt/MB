package com.whzl.mengbi.ui.view;

import com.whzl.mengbi.model.entity.RechargeInfo;

public interface RechargeView {
    void showChannelInfo(RechargeInfo rechargeInfo);
    void showOrderInfo(int orderId,String token);
}
