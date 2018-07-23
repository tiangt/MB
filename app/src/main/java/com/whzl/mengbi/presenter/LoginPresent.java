package com.whzl.mengbi.presenter;

import java.util.HashMap;

/**
 * Class Note:登陆的Presenter 的接口，实现类为LoginPresenterImpl，完成登陆的验证，以及销毁当前view
 */
public interface LoginPresent {

    void login(HashMap hashMap);

    void onDestroy();

    void openLogin(HashMap hashMap);
}