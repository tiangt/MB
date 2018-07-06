package com.whzl.mengbi.presenter.impl;

import com.whzl.mengbi.model.RegisterModel;
import com.whzl.mengbi.model.entity.RegisterInfo;
import com.whzl.mengbi.model.impl.RegisterModelImpl;
import com.whzl.mengbi.presenter.OnRegisterFinishedListener;
import com.whzl.mengbi.presenter.RegisterPresenter;
import com.whzl.mengbi.ui.view.RegisterView;

public class RegisterPresenterImpl implements RegisterPresenter ,OnRegisterFinishedListener{
    private RegisterView registerView;
    private RegisterModel registerModel;

    public RegisterPresenterImpl(RegisterView registerView){
        this.registerView = registerView;
        registerModel = new RegisterModelImpl();
    }


    @Override
    public void getRegexCode(String mobile) {
        registerModel.doRegexCode(mobile,this);
    }

    @Override
    public void getRegister(String username, String password, String code) {
        registerModel.doRegister(username,password,code,this);
    }

    @Override
    public void onRegexCodeSuccess(String code,String msg) {
        if(registerView != null){
            registerView.showRegexCodeMsg(code,msg);
        }
    }

    @Override
    public void onSuccess(RegisterInfo registerInfo) {
        if(registerView != null){
            registerView.navigateToAll(registerInfo);
        }
    }

    @Override
    public void onError(String msg) {
        if(registerView != null){
            registerView.onError(msg);
        }
    }

    @Override
    public void onDestroy() {
        registerView = null;
    }
}
