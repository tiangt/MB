package com.whzl.mengbi.presenter;

import java.util.HashMap;

public interface UserInfoPresenter {
    void onUpdataPortrait(String userId, String filename);

    void onUpdataNickName(String userId, String nickname);

    void onUpdataUserInfo(HashMap hashMap);

    void onDestory();

    void onUpdateSign(String userId, String sign);
}
