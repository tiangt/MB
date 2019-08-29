package com.whzl.mengbi.model;

import com.whzl.mengbi.presenter.OnLoginFinishedListener;

import java.util.HashMap;

/**
 * Class Note:登录的操作的接口，实现类为LoginModelImpl.相当于MVP模式中的Model层
 */
public interface LoginModel {

    void doLogin(HashMap hashMap, OnLoginFinishedListener listener);

    void openLogin(HashMap hashMap, OnLoginFinishedListener listener);

}
