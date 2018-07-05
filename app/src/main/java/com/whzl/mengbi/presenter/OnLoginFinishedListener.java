package com.whzl.mengbi.presenter;

import com.whzl.mengbi.model.entity.VisitorUserInfo;
import com.whzl.mengbi.model.entity.UserInfo;

/**
 * Class Note:登陆事件监听
 */
public interface OnLoginFinishedListener {

    /**
     * 游客请求成功回调
     */
    void onVisitorLoginSuccess();

    /**
     * 用户请求成功回调
     */
    void onSuccess();

    /**
     * 请求失败回调
     */
    void onError(String error);
}
