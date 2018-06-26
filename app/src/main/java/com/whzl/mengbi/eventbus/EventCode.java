package com.whzl.mengbi.eventbus;

public final class EventCode {

    /**
     * 公告
     */
    public static final int SEND_SYSTEM_MESSAGE=-1;

    /**
     * 聊天
     * 公聊 common 0
     */
    public static final int CHAT_COMMON=0;//公聊

    /**
     * 私聊 private 1
     */
    public static final int CHAT_PRIVATE=1;//私聊

    /**
     * 游客入场
     * 2
     */
    public static final int  TOURIST=2;

    /**
     * 登录用户入场
     * 3
     */
    public static final int  LOGIN_USER=3;

    /**
     * 送礼
     * 4
     */
    public static final int  GIVE_GIFT=4;

    /**
     * 关注
     * 5
     */
    public static final int  FOLLOW=5;

    /**
     * 禁言
     * 6
     */
    public static final int  AN_EXCUSE=6;

    /**
     * 踢人
     * 7
     */
    public static final int  KICKING=2;

    /**
     * 升为房管
     * 8
     */
    public static final int  FANG_GUAN=8;
}
