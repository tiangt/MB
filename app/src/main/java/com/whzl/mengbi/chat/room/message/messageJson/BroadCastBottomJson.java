package com.whzl.mengbi.chat.room.message.messageJson;

/**
 * @author nobody
 * @date 2018/9/27
 */
public class BroadCastBottomJson {


    /**
     * context : {"anchorId":30000131,"anchorNickname":"土豪30000131","isGuard":false,"isVip":false,"levelValue":14,"message":"*","nickname":"土豪30000157","programId":100095,"userId":30000157}
     */

    public ContextBean context;

    public static class ContextBean {
        /**
         * anchorId : 30000131
         * anchorNickname : 土豪30000131
         * isGuard : false
         * isVip : false
         * levelValue : 14
         * message : *
         * nickname : 土豪30000157
         * programId : 100095
         * userId : 30000157
         */

        public int anchorId;
        public String anchorNickname;
        public boolean isGuard;
        public boolean isVip;
        public int levelValue;
        public String message;
        public String nickname;
        public int programId;
        public int userId;
    }
}
