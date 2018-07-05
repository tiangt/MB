package com.whzl.mengbi.model;

import com.whzl.mengbi.presenter.OnRegisterFinishedListener;

public interface RegisterModel {
    void doRegexCode(String mobile, OnRegisterFinishedListener listener);

    void doRegister(String username,String password,String code,OnRegisterFinishedListener listener);
}
