package com.whzl.mengbi.model;

import com.whzl.mengbi.presenter.OnUserInfoFInishedListener;
import com.whzl.mengbi.presenter.impl.UserInfoPresenterImpl;

import java.util.HashMap;

public interface UserInfoModel {
    void doUpdataPortrait(String userId,String filename, OnUserInfoFInishedListener listener);

    void doUpdataNickName(String userId,String nickname,OnUserInfoFInishedListener listener);

    void doUpdataUserInfo(HashMap hashMap,OnUserInfoFInishedListener listener);

    void doUpdateSign(String userId, String sign, OnUserInfoFInishedListener listener);
}
