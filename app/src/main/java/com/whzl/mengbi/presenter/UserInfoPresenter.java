package com.whzl.mengbi.presenter;

public interface UserInfoPresenter {
    void onUpdataPortrait(String userId,String filename);

    void onUpdataNickName(String userId,String nickname);

    void onUpdataSex(String userId, String sex);

    void onUpdataAddress(String userId, String province, String city);

    void onUpdataBirthday(String userId, String birthday);

    void onDestory();
}
