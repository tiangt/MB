package com.whzl.mengbi.presenter.impl;

import com.whzl.mengbi.model.RechargeModel;
import com.whzl.mengbi.model.entity.RechargeInfo;
import com.whzl.mengbi.model.impl.RechargeModelImpl;
import com.whzl.mengbi.presenter.RechargePresenter;
import com.whzl.mengbi.presenter.OnRechargeFinishedListener;
import com.whzl.mengbi.ui.view.RechargeView;

import java.util.HashMap;

public class RechargePresenterImpl implements RechargePresenter,OnRechargeFinishedListener {
    private RechargeView rechargeView;
    private RechargeModel rechargeModel;

    public RechargePresenterImpl(RechargeView rechargeView){
        this.rechargeView = rechargeView;
        this.rechargeModel = new RechargeModelImpl();
    }


    @Override
    public void getChannelInfo(HashMap hashMap) {
        rechargeModel.doRechargeChannel(hashMap,this);
    }

    @Override
    public void getOrderInfo(HashMap hashMap) {
        rechargeModel.doRechargeOrder(hashMap,this);
    }

    @Override
    public void onChannelSuccess(RechargeInfo rechargeInfo) {
        if(rechargeView != null){
            rechargeView.showChannelInfo(rechargeInfo);
        }
    }

    @Override
    public void onOrderSuccess(int orderId, String token) {
        if(rechargeView != null){
            rechargeView.showOrderInfo(orderId,token);
        }
    }

    @Override
    public void onDestroy() {
        rechargeView = null;
    }
}