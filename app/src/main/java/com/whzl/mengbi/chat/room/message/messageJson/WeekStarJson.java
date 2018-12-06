package com.whzl.mengbi.chat.room.message.messageJson;

/**
 * @author nobody
 * @date 2018/12/6
 */
public class WeekStarJson {

    /**
     * context : {"nickName":"yyyyy","rank":1,"ranksId":37060,"speakcolor":"true","userId":"30000098"}
     */

    public ContextBean context;

    public static class ContextBean {
        /**
         * nickName : yyyyy
         * rank : 1
         * ranksId : 37060
         * speakcolor : true
         * userId : 30000098
         */

        public String nickName;
        public int rank;
        public int ranksId;
        public String speakcolor;
        public String userId;
        public String giftName;
    }
}
