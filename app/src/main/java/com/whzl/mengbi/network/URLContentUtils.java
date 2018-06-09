package com.whzl.mengbi.network;

public class URLContentUtils{

    /**
     * 请求接口根地址
     */
    public static final String BASE_URL = "https://t2-api.mengbitv.com/";


    /**
     * 主播登录接口
     */
    public static final String USER_ANCHOR_LOGIN="v1/user/anchor-login";

    /**
     * 匿名登录接口
     */
    public static final String USER_VISITOR_LOGIN="v1/user/visitor-login";

    /**
     * 用户登录接口
     */
    public static final String USER_NORMAL_LOGIN="v1/user/normal-login";

    /**
     * 首页轮播广告接口
     */
    public static final String  INDEX_ADV= "v1/adv/index-adv";


    /**
     * 推荐主播接口
     */
    public static final String  RECOMMEND_ANCHOR= "v1/anchor/recommend-anchor";

    /**
     * 主播展示接口
     */
    public static final String  SHOW_ANCHOR= "v1/anchor/show-anchor";

    /**
     * 用户注册接口
     */
    public static final String  REGISTER= "v1/user/register";
    /**
     * 注册验证码接口
     */
    public static final String  SEND_CODE= "v1/user/send-code";


}
