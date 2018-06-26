package com.whzl.mengbi.network;

public class URLContentUtils{

    /**
     * 测试请求接口根地址
     */
    public static final String BASE_URL = "https://t2-api.mengbitv.com/";

    /**
     * 勋章图片地址
     * http://t2-api.mengbitv.com"//
     * https://test-api.mengbitv.com"//测试
     * https://test-img.mengbitv.com
     * http://api.mengbitv.com" //正式服
     * https://img.mengbitv.com"
     */
      public static final String BASE_IMAGE_URL="https://test-img.mengbitv.com/";

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

    /**
     * 礼物展示接口
     */
    public static final String  GIFT_LIST= "v1/gift/gift-list";

    /**
     *获取直播间token接口
     */
    public static final String  CHECK_ENTERR_ROOM= "v1/room/check-enter-room";

    /**
     * 获取用户详情接口
     */
    public static final String GET_USER_INFO="v1/user/get-user-info";
}
