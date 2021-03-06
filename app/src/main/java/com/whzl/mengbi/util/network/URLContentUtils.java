package com.whzl.mengbi.util.network;

import com.whzl.mengbi.BuildConfig;

public class URLContentUtils {

    public static boolean isDebug = BuildConfig.API_DEBUG_ENT;


    /**
     * 接口根地址
     */
    public static final String DEBUG_BASE_URL = "https://t2-api.mengbitv.com/";
    public static final String RELEASE_BASE_URL = "https://api.mengbitv.com/";

    /**
     * 勋章图片地址
     */
    public static final String DEBUG_BASE_IMAGE_URL = "https://test-img.mengbitv.com/";
    public static final String RELEASE_BASE_IMAGE_URL = "https://img.mengbitv.com/";

    /**
     * 匿名登录接口
     */
    public static final String USER_VISITOR_LOGIN = "v1/user/visitor-login";

    /**
     * 用户登录接口
     */
    public static final String USER_NORMAL_LOGIN = "v1/user/normal-login";

    /**
     * 首页轮播广告接口
     */
    public static final String INDEX_ADV = "v1/adv/index-adv";


    /**
     * 推荐主播接口
     */
    public static final String RECOMMEND_ANCHOR = "v1/anchor/recommend-anchor";

    /**
     * 主播展示接口
     */
    public static final String SHOW_ANCHOR = "v1/anchor/show-anchor";

    /**
     * 用户注册接口
     */
    public static final String REGISTER = "v1/user/register";
    /**
     * 注册验证码接口
     */
    public static final String SEND_CODE = "v1/user/send-code";

    /**
     * 礼物展示接口
     */
    public static final String GIFT_LIST = "v1/gift/gifts";

    /**
     * 获取直播间token接口
     */
    public static final String CHECK_ENTERR_ROOM = "v1/room/check-enter-room";

    /**
     * 获取用户详情接口
     */
    public static final String GET_USER_INFO = "v1/user/get-user-info";

    /**
     * 获修改用户基本信息接口
     */
    public static final String MODIFY_USER_INFO = "v1/user/modify-user-info";

    /**
     * 修改昵称接口
     */
    public static final String MODIFY_NICKNAME = "v1/user/modify-nickname";

    /**
     * 修改用户头像接口
     */
    public static final String MODIFY_AVATAR = "v1/user/modify-avatar";

    /**
     * 第三方登录
     */
    public static final String OPEN_LOGIN = "v1/user/open-login";

    /**
     * 移动端充值规则接口
     */
    public static final String RECHARGE_GET_CHANNEL = "v1/recharge/get-channel";

    /**
     * 支付宝下单接口
     */
    public static final String RECHARGE_ORDER = "v1/recharge/order";

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
    public static final String ROOM_USER_INFO = "v1/room/room-user";


    /**
     * 直播间的在线观看用户
     */
    public static final String ROOM_ONLINE = "v1/room/online";

    /**
     * 直播间贡献排行
     */
    public static final String CONTRIBUTION_LIST = "v1/rank/room-rank-new";

    /**
     * 直播间贡献排行
     */
    public static final String SEND_GIFT = "v1/consume/send-gift";

    /**
     * 检查登录
     */
    public static final String CHECK_LOGIN = "v1/user/check-login";

    /**
     * 获取关注主播列表
     */
    public static final String ANCHOR_FOLLOWED = "v1/program/get-my-programs";

    /**
     * 获取关注主播列表
     */
    public static final String CHECK_UPDATE = "v1/version/version-android";

    /**
     * 退出登录
     */
    public static final String LOGOUT = "v1/user/logout";

    /**
     * 取消关注
     */
    public static final String UNFOLLOW_ANCHOR = "v1/room/remove-user-program";

    /**
     * 跑道
     */

    public static final String SITE_TIME = "site/time";

    public static String getBaseUrl() {
        return isDebug ? DEBUG_BASE_URL : RELEASE_BASE_URL;
    }

    public static String getBaseImageUrl() {
        return isDebug ? DEBUG_BASE_IMAGE_URL : RELEASE_BASE_IMAGE_URL;
    }

    /**
     * 忘记密码发送验证码
     */
    public static final String FORGET_PASS_CODE = "v1/user/forget-pass-code";

    /**
     * 重置密码
     */
    public static final String RESET_PSW = "v1/user/reset-pass";


    /**
     * 验证手机号是否存在
     */
    public static final String IS_PHONE = "v1/user/check-identify-code";

    /**
     * MVP设置惩罚方式
     */
    public static final String PUNISH_WAY = "/v1/pk/set-punish-way";

    /**
     * 关注主播
     */
    public static final String SOCIAL_FOLLOW = "v1/social/follow";

    /**
     * 获取用户主页
     */
    public static final String HOME_PAGE = "v1/user/homepage";

    /**
     * 举报原因
     */
    public static final String REPORT_REASON = "v1/social/report-reason";

    /**
     * 举报
     */
    public static final String REPORT = "v1/social/report";

    /**
     * 主播头条排名榜单
     */
    public static final String HEADLINE_LIST = "v1/rank/head-line-rank-list";

    /**
     * 首页主播头条排名
     */
    public static final String HEADLINE_TOP = "v1/anchor/head-line-top-list";

    /**
     * 周星礼物列表
     */
    public static final String WEEKSTAR_GIFT = "v1/rank/week-star-gift-list";

    /**
     * 周星榜单
     */
    public static final String WEEKSTAR_RANK = "v1/rank/week-star-rank-list";

    /**
     * 获取排行榜主播超越第一名需要的萌币数和礼物数量
     */
    public static final String RANK_BEYOND_FIRST = "v1/rank/rank-list-beyond-first-val";

    /**
     *
     */
    public static final String GOODS_PRICE = "v1/goods/goods-price";

    /**
     * 获取用户的碎片合成列表
     */
    public static final String COMPOSITE_LIST = "v1/debris/composite-list";

    /**
     * 合成碎片
     */
    public static final String COMPOSITE_CHIP = "v1/debris/composition";

    /**
     * 我的碎片
     */
    public static final String MY_DEBRIS_LIST = "v1/debris/my-debris-list";

    /**
     * 绑定手机或者邮箱
     */
    public static final String BIND_PHONE = "v1/user/bind-email-or-mobile";

    /**
     * 更改手机号--验证验证码
     */
    public static final String VERIFY_CHECK_CODE = "v1/user/verify-check-code";

    /**
     * 增加主播热搜度
     */
    public static final String SAVE_ANCHOR_SEARCH = "v1/anchor/save-anchor-heat-degree";

    /**
     * 获取热搜主播列表
     */
    public static final String TRENDING_ANCHOR = "v1/anchor/heat-degree-list";

    /**
     * 榜单
     */
    public static final String FIND_RANK = "v1/rank/rank";
}
