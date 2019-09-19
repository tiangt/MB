package com.whzl.mengbi.chat.room.message.messageJson;

/**
 * @author nobody
 * @date 2019-09-18
 */
public class UserRedpacketAwardJson {

    /**
     * context : {"userInfoDto":{"userId":30000165,"nickname":"26","lastUpdateTime":1545966468430,"awardUserId":30000805,"awardNickname":"萌友30000805"},"busicode":"USER_REDPACKET_AWARD","uGameRedpacketDto":{"sendGoodsNum":0,"gmtModified":1568708732000,"sendPrice":0,"lotteryDrawId":48,"awardGoodsId":0,"gmtCreated":1568708732000,"awardTotalPrice":10000,"sendGoodsId":94329,"userId":30000165,"awardPeopleNum":10,"sendGoodsName":"静一静","awardGoodsNum":0,"busiStatus":"START","closeTime":1568708912000,"sendType":"GOODS","startTime":1568708732000,"programId":100165,"awardType":"COIN"},"gameRedpacketAwardDto":{"awardType":"COIN","awardGoodsId":0,"awardGoodsNum":0,"awardPrice":2000}}
     */

    public ContextBean context;

    public static class ContextBean {
        /**
         * userInfoDto : {"userId":30000165,"nickname":"26","lastUpdateTime":1545966468430,"awardUserId":30000805,"awardNickname":"萌友30000805"}
         * busicode : USER_REDPACKET_AWARD
         * uGameRedpacketDto : {"sendGoodsNum":0,"gmtModified":1568708732000,"sendPrice":0,"lotteryDrawId":48,"awardGoodsId":0,"gmtCreated":1568708732000,"awardTotalPrice":10000,"sendGoodsId":94329,"userId":30000165,"awardPeopleNum":10,"sendGoodsName":"静一静","awardGoodsNum":0,"busiStatus":"START","closeTime":1568708912000,"sendType":"GOODS","startTime":1568708732000,"programId":100165,"awardType":"COIN"}
         * gameRedpacketAwardDto : {"awardType":"COIN","awardGoodsId":0,"awardGoodsNum":0,"awardPrice":2000}
         */

        public UserInfoDtoBean userInfoDto;
        public String busicode;
        public UGameRedpacketDtoBean uGameRedpacketDto;
        public GameRedpacketAwardDtoBean gameRedpacketAwardDto;

        public static class UserInfoDtoBean {
            /**
             * userId : 30000165
             * nickname : 26
             * lastUpdateTime : 1545966468430
             * awardUserId : 30000805
             * awardNickname : 萌友30000805
             */

            public int userId;
            public String nickname;
            public long lastUpdateTime;
            public int awardUserId;
            public String awardNickname;
        }

        public static class UGameRedpacketDtoBean {
            /**
             * sendGoodsNum : 0
             * gmtModified : 1568708732000
             * sendPrice : 0
             * lotteryDrawId : 48
             * awardGoodsId : 0
             * gmtCreated : 1568708732000
             * awardTotalPrice : 10000
             * sendGoodsId : 94329
             * userId : 30000165
             * awardPeopleNum : 10
             * sendGoodsName : 静一静
             * awardGoodsNum : 0
             * busiStatus : START
             * closeTime : 1568708912000
             * sendType : GOODS
             * startTime : 1568708732000
             * programId : 100165
             * awardType : COIN
             */

            public int sendGoodsNum;
            public long gmtModified;
            public int sendPrice;
            public int lotteryDrawId;
            public int awardGoodsId;
            public long gmtCreated;
            public int awardTotalPrice;
            public int sendGoodsId;
            public int userId;
            public int awardPeopleNum;
            public String sendGoodsName;
            public int awardGoodsNum;
            public String awardGoodsName;
            public String busiStatus;
            public long closeTime;
            public String sendType;
            public long startTime;
            public int programId;
            public String awardType;
        }

        public static class GameRedpacketAwardDtoBean {
            /**
             * awardType : COIN
             * awardGoodsId : 0
             * awardGoodsNum : 0
             * awardPrice : 2000
             */

            public String awardType;
            public int awardGoodsId;
            public int awardGoodsNum;
            public long awardPrice;
        }
    }
}
