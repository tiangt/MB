package com.whzl.mengbi.config;

/**
 * @author shaw
 * @date 2018/7/19
 */
public interface NetConfig {
    int DEFAULT_PAGER_SIZE = 20;
    String FLAG_ALI_PAY = "AlipayMobile";
    String FLAG_WECHAT_PAY = "WeixinApp";
    String FLAG_ACTIVE = "ACTIVE";
    int CODE_ALI_PAY_SUCCESS = 9000;
    int CODE_ALI_PAY_CANCEL = 6001;
    int CODE_WE_CHAT_PAY_SUCCESS = 0;
    int CODE_WE_CHAT_PAY_CANCEL = -2;
    int CODE_WE_CHAT_PAY_FAIL = -1;
    String OPEN_LOGIN_WEIXIN = "WEIXIN";
    String OPEN_LOGIN_QQ = "QQ";
    String LEVEL_TYPE_ANCHOR = "ANCHOR_LEVEL";
    String LEVEL_TYPE_USER = "USER_LEVEL";
    String USER_TYPE_ANCHOR = "ANCHOR";
}
