package com.whzl.mengbi.presenter;

public interface RegisterPresenter {
    void getRegexCode(String mobile);

    void getRegister(String username,String password,String code);

    void onDestroy();
}
