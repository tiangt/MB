package com.whzl.mengbi.util.network;

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

    /**
     * 获修改用户基本信息接口
     */
    public static final String MODIFY_USER_INFO="v1/user/modify-user-info";

    /**
     * 修改昵称接口
     */
    public static final String MODIFY_NICKNAME="v1/user/modify-nickname";

    /**
     * 修改用户头像接口
     */
    public static final String MODIFY_AVATAR="v1/user/modify-avatar";

    /**
     * 第三方登陆
     */
    public static final String OPEN_LOGIN="v1/user/open-login";

    /**
     * 移动端充值规则接口
     */
    public static final String RECHARGE_GET_CHANNEL="v1/recharge/get-channel";

    /**
     * 支付宝下单接口
     */
    public static final String RECHARGE_ORDER="v1/recharge/order";

    /**
     * 获取房间信息
     */
    public static final String ROOM_INFO = "v1/room/get-room-info";

    /**
     * 获取房间观看人数
     */
    public static final String AUDIENCE_COUNT = "v1/room/total-audience";

    /**
     * 关注主播
     */
    public static final String FELLOW_HOST = "v1/room/add-sub";

    /**
     * 直播间用户信息
     */
    public static final String ROOM_USER_INFO = "v1/room/get-enter-user-info";


    /**
     * 直播间的在线观看用户
     */
    public static final String ROOM_ONLINE = "v1/room/online";
}
