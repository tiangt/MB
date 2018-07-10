package com.whzl.mengbi.model;

import com.whzl.mengbi.presenter.OnRechargeFinishedListener;

import java.util.HashMap;

public interface RechargeModel {
    void doRechargeChannel(HashMap hashMap, OnRechargeFinishedListener listener);
    void doRechargeOrder(HashMap hashMap,OnRechargeFinishedListener listener);
}
