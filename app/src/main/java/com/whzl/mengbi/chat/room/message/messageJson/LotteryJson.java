package com.whzl.mengbi.chat.room.message.messageJson;


public class LotteryJson {

    /**
     * context : {"awardContentUnit":"个","awardContentNum":"1","nickname":"土豪30000096","awardContentName":"亲一亲","type":"LOTTERY","userId":30000096}
     */

    public ContextBean context;

    public static class ContextBean {
        /**
         * awardContentUnit : 个
         * awardContentNum : 1
         * nickname : 土豪30000096
         * awardContentName : 亲一亲
         * type : LOTTERY
         * userId : 30000096
         */

        public String awardContentUnit;
        public String awardContentNum;
        public String nickname;
        public String awardContentName;
        public String type;
        public int userId;
    }
}
