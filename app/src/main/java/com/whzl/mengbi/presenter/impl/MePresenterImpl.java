package com.whzl.mengbi.presenter.impl;

import com.whzl.mengbi.model.MeModel;
import com.whzl.mengbi.model.entity.UserInfo;
import com.whzl.mengbi.model.impl.MeModelImpl;
import com.whzl.mengbi.presenter.MePresenter;
import com.whzl.mengbi.presenter.OnMeFinishedListener;
import com.whzl.mengbi.ui.view.MeView;


public class MePresenterImpl implements MePresenter,OnMeFinishedListener{
    private MeView meView;
    private MeModel meModel;

    public MePresenterImpl(MeView meView){
        this.meView = meView;
        meModel = new MeModelImpl();
    }

    @Override
    public void getUserInfo() {
        meModel.doUserInfo(this);
    }

    @Override
    public void onSuccess(UserInfo userInfo) {
        if(meView != null){
            meView.showUserInfo(userInfo);
        }
    }


    @Override
    public void onDestroy() {
        meView = null;
    }
}
