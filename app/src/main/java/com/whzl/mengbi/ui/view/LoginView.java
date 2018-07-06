package com.whzl.mengbi.ui.view;


/**
 * Class Note:登陆View的接口，实现类也就是登陆的activity
 */
public interface LoginView {

    void visitorNavigateToHome();

    void navigateToHome();

    void showError(String msg);
}
