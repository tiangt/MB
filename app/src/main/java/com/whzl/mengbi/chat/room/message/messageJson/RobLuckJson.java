package com.whzl.mengbi.chat.room.message.messageJson;

/**
 * @author nobody
 * @date 2019/3/8
 */
public class RobLuckJson {

    /**
     * context : {"busiCode":"prize_pool_change","prizePoolNumber":200}
     * type : busi.msg
     * platform : WEB,MOBILE
     */

    public ContextBean context;
    public String type;
    public String platform;

    public static class ContextBean {
        /**
         * busiCode : prize_pool_change
         * prizePoolNumber : 200
         */

        public String busiCode;
        public String userNickName;
        public String giftName;
        public int giftNumber;
        public int prizePoolNumber;
    }
}
