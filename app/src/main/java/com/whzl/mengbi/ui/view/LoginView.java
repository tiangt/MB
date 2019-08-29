package com.whzl.mengbi.ui.view;


import com.whzl.mengbi.model.entity.UserInfo;

/**
 * Class Note:登录View的接口，实现类也就是登录的activity
 */
public interface LoginView {

    void showError(String msg);

    void loginSuccess(UserInfo userInfo);
}
