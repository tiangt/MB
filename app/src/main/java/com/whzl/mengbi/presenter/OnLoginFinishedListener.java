package com.whzl.mengbi.presenter;

import com.whzl.mengbi.model.entity.UserInfo;

/**
 * Class Note:登陆事件监听
 */
public interface OnLoginFinishedListener {

    /**
     * 用户请求成功回调
     */
    void onLoginSuccess(UserInfo userInfo);

    /**
     * 请求失败回调
     */
    void onError(String error);
}
