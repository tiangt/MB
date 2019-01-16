package com.whzl.mengbi.chat.room.message.messageJson;

/**
 * @author nobody
 * @date 2019/1/16
 */
public class RedPackJson {

    /**
     * context : {"messageSubType":"broadcast","sendObjectType":"USER","redPacketType":"RANDOM","founderUserId":30000536,"expDate":1547540267926,"busiCodeName":"USER_SEND_REDPACKET","effDate":1547538467926,"sendObjectId":30000530,"sendObjectLastUpdateTime":1547523669100,"programTreasureNum":80000,"founderUserNickname":"萌友30000536","sendObjectNickname":"逍遥子","contentType":"COIN","redPacketId":"yjuEW9Hki+5/6+c9U20LwIIDCWvXR1lbcqwQXTyeZPzF5FQ7B9LmDDVrszl4DBsA","programId":100147,"leftSeconds":1800}
     */

    public ContextBean context;

    public static class ContextBean {
        /**
         * messageSubType : broadcast
         * sendObjectType : USER
         * redPacketType : RANDOM
         * founderUserId : 30000536
         * expDate : 1547540267926
         * busiCodeName : USER_SEND_REDPACKET
         * effDate : 1547538467926
         * sendObjectId : 30000530
         * sendObjectLastUpdateTime : 1547523669100
         * programTreasureNum : 80000
         * founderUserNickname : 萌友30000536
         * sendObjectNickname : 逍遥子
         * contentType : COIN
         * redPacketId : yjuEW9Hki+5/6+c9U20LwIIDCWvXR1lbcqwQXTyeZPzF5FQ7B9LmDDVrszl4DBsA
         * programId : 100147
         * leftSeconds : 1800
         */

        public String messageSubType;
        public String sendObjectType;
        public String redPacketType;
        public int founderUserId;
        public long expDate;
        public String busiCodeName;
        public String returnObjectNickname;
        public long effDate;
        public int sendObjectId;
        public long sendObjectLastUpdateTime;
        public long programTreasureNum;
        public String founderUserNickname;
        public String sendObjectNickname;
        public String contentType;
        public String redPacketId;
        public int programId;
        public int leftSeconds;
    }
}
