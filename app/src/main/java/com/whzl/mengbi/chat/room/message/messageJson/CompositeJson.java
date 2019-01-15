package com.whzl.mengbi.chat.room.message.messageJson;

/**
 * @author cliang
 * @date 2019.1.15
 */
public class CompositeJson {

    /**
     * context : {"awardContentName":"跑车","awardContentNum":"1","awardContentUnit":"个","nickname":"尽量长一点是坠好滴","userId":30000724}
     *
     */

    public ContextBean context;

    public static class ContextBean {
        /**
         * awardContentName : 跑车
         * awardContentNum : 1
         * awardContentUnit : 个
         * nickname : 尽量长一点是坠好滴
         * userId : 30000724
         */

        public String awardContentName;
        public String awardContentNum;
        public String awardContentUnit;
        public String nickname;
        public int userId;
    }
}
