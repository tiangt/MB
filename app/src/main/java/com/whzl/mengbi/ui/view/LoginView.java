package com.whzl.mengbi.ui.view;


import com.whzl.mengbi.model.entity.UserInfo;

/**
 * Class Note:登陆View的接口，实现类也就是登陆的activity
 */
public interface LoginView {

    void showError(String msg);

    void loginSuccess(UserInfo userInfo);
}
