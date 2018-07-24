package com.whzl.mengbi.presenter.impl;

import com.whzl.mengbi.model.UserInfoModel;
import com.whzl.mengbi.model.impl.UserInfoModelImpl;
import com.whzl.mengbi.presenter.OnUserInfoFInishedListener;
import com.whzl.mengbi.presenter.UserInfoPresenter;
import com.whzl.mengbi.ui.view.UserInfoView;

import java.util.HashMap;

public class UserInfoPresenterImpl implements UserInfoPresenter,OnUserInfoFInishedListener {
    private UserInfoView userInfoView;
    private UserInfoModel userInfoModel;

    public  UserInfoPresenterImpl(UserInfoView userInfoView){
        this.userInfoView = userInfoView;
        this.userInfoModel = new UserInfoModelImpl();
    }

    @Override
    public void onUpdataPortrait(String userId,String filename) {
        userInfoModel.doUpdataPortrait(userId,filename,this);
    }

    @Override
    public void onUpdataNickName(String userId,String nickname) {
        userInfoModel.doUpdataNickName(userId,nickname,this);
    }

    @Override
    public void onUpdataUserInfo(HashMap hashMap) {
        userInfoModel.doUpdataUserInfo(hashMap,this);
    }


    @Override
    public void onPortraitSuccess(String filename) {
        if(userInfoView != null){
            userInfoView.showPortrait(filename);
        }
    }

    @Override
    public void onSuccess(String msg) {
        if(userInfoView != null){
            userInfoView.showSuccess(msg);
        }
    }

    @Override
    public void onError(String msg) {
        if(userInfoView != null){
            userInfoView.showError(msg);
        }
    }

    @Override
    public void onModifyNicknameSuc(String nickname) {
        if (userInfoView != null) {
            userInfoView.onModifyNickname(nickname);
        }
    }

    @Override
    public void onDestory() {
        userInfoView = null;
    }
}
