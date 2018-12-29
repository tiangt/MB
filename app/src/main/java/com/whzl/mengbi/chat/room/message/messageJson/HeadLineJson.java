package com.whzl.mengbi.chat.room.message.messageJson;

/**
 * @author nobody
 * @date 2018/9/27
 */
public class HeadLineJson {


    /**
     * context : {"lastUpdateTime":1531102704660,"nickname":"萌友30000536","programId":100147,"rank":1,"rankingsId":36400,"userId":30000536}
     */

    public ContextBean context;

    public static class ContextBean {
        /**
         * lastUpdateTime : 1531102704660
         * nickname : 萌友30000536
         * programId : 100147
         * rank : 1
         * rankingsId : 36400
         * userId : 30000536
         */

        public long lastUpdateTime;
        public String nickname;
        public int programId;
        public int rank;
        public int rankingsId;
        public int userId;
    }
}
