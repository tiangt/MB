package com.whzl.mengbi.presenter.impl;

import com.whzl.mengbi.model.LoginModel;
import com.whzl.mengbi.model.entity.UserInfo;
import com.whzl.mengbi.model.impl.LoginModelImpl;
import com.whzl.mengbi.presenter.LoginPresent;
import com.whzl.mengbi.presenter.OnLoginFinishedListener;
import com.whzl.mengbi.ui.view.LoginView;

import java.util.HashMap;

/**
 * Class Note:
 * 1 完成presenter的实现。这里面主要是Model层和View层的交互和操作。
 * 2  presenter里面还有个OnLoginFinishedListener，
 * 其在Presenter层实现，给Model层回调，更改View层的状态，
 * 确保 Model层不直接操作View层。如果没有这一接口在LoginPresenterImpl实现的话，
 * LoginPresenterImpl只 有View和Model的引用那么Model怎么把结果告诉View呢？
 */

public class LoginPresenterImpl implements LoginPresent, OnLoginFinishedListener {
    private LoginView loginView;
    private LoginModel loginModel;

    public LoginPresenterImpl(LoginView loginView) {
        this.loginView = loginView;
        this.loginModel = new LoginModelImpl();
    }


    @Override
    public void login(HashMap hashMap) {
        loginModel.doLogin(hashMap, this);
    }

    @Override
    public void openLogin(HashMap hashMap) {
        loginModel.openLogin(hashMap, this);
    }

    @Override
    public void onLoginSuccess(UserInfo userInfo) {
        if (loginView != null) {
            loginView.loginSuccess(userInfo);
        }
    }

    @Override
    public void onError(String error) {
        if (loginView != null) {
            loginView.showError(error);
        }
    }

    @Override
    public void onDestroy() {
        loginView = null;
    }

}
