package com.whzl.mengbi.model;

import com.whzl.mengbi.presenter.OnUserInfoFInishedListener;

import java.util.HashMap;

public interface UserInfoModel {
    void doUpdataPortrait(String userId,String filename, OnUserInfoFInishedListener listener);

    void doUpdataNickName(String userId,String nickname,OnUserInfoFInishedListener listener);

    void doUpdataUserInfo(HashMap hashMap,OnUserInfoFInishedListener listener);
}
