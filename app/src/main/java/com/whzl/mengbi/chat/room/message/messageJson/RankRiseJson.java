package com.whzl.mengbi.chat.room.message.messageJson;

/**
 * @author nobody
 * @date 2019/3/1
 */
public class RankRiseJson {

    /**
     * context : {"nickName":"社会一姐社会一姐社会","rankingsName":"最美女神","rank":1,"oldUserObject":{"nickName":"萌友30000803","rank":1,"userId":"30000803"},"userId":"30000139","speakcolor":"true","ranksId":37138}
     */

    public ContextBean context;

    public static class ContextBean {
        /**
         * nickName : 社会一姐社会一姐社会
         * rankingsName : 最美女神
         * rank : 1
         * oldUserObject : {"nickName":"萌友30000803","rank":1,"userId":"30000803"}
         * userId : 30000139
         * speakcolor : true
         * ranksId : 37138
         */

        public String nickName;
        public String rankingsName;
        public int rank;
        public OldUserObjectBean oldUserObject;
        public String userId;
        public String speakcolor;
        public int ranksId;

        public static class OldUserObjectBean {
            /**
             * nickName : 萌友30000803
             * rank : 1
             * userId : 30000803
             */

            public String nickName;
            public int rank;
            public String userId;
        }
    }
}
