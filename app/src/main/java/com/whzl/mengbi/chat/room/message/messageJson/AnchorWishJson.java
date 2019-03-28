package com.whzl.mengbi.chat.room.message.messageJson;

/**
 * @author nobody
 * @date 2019/3/28
 */
public class AnchorWishJson {

    /**
     * context : {"busicode":"RANK_CHANGE","programId":100001,"rankPeopleNum":1,"totalScore":100}
     * eventCode : GAME_WISH
     * msgType : EVENT
     * platform : WEB,MOBILE
     * type : busi.msg
     */

    public String eventCode;
    public String msgType;
    public String platform;
    public String type;
    /**
     * context : {"busicode":"WISH_SUCCESS","nickName":"32修改昵称","programId":100001,"userId":30000012,"wishGiftName":"蓝色妖姬"}
     */

    public ContextBean context;

    public static class ContextBean {
        /**
         * busicode : RANK_CHANGE
         * programId : 100001
         * rankPeopleNum : 1
         * totalScore : 100
         */

        public String busicode;
        public String nickName;
        public int programId;
        public int userId;
        public int rankPeopleNum;
        public int totalScore;
        public String wishGiftName;
    }


}
