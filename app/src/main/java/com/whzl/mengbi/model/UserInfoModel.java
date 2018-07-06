package com.whzl.mengbi.model;

import com.whzl.mengbi.presenter.OnUserInfoFInishedListener;

public interface UserInfoModel {
    void doUpdataPortrait(String userId,String filename, OnUserInfoFInishedListener listener);

    void doUpdataNickName(String userId,String nickname,OnUserInfoFInishedListener listener);

    void doUpdataSex(String userId,String sex,OnUserInfoFInishedListener listener);

    void doUpdataAddress(String userId,String province,String city,OnUserInfoFInishedListener listener);

    void doUpdataBirthday(String userId,String birthday,OnUserInfoFInishedListener listener);

    void doUpdataIntroduce(String userId,String introduce,OnUserInfoFInishedListener listener);
}
