package com.whzl.mengbi.chat.room.message.messageJson;

/**
 * @author nobody
 * @date 2019-09-17
 */
public class UserRedpacketJson {


    /**
     * context : {"userInfo":{"qq":"1663238413","accountPlatformId":"MOBILE","anchorPlatformType":"ENT","ip":"183.129.167.114","sex":"W","mobile":"13222220028","registerType":"MOBILE","userId":30000165,"lastLoginTime":1564021341327,"coverId":1,"createTime":1525232404000,"certName":"发放","mobileStatus":"BIND","loginName":"mobile_30000165","nickname":"26","userType":"ANCHOR","certNum":"445101199409061853","lastPlatformType":"PC_WEB","anchorType":"NEW","lastRechargeTime":1561428727496,"lastUpdateTime":1555407580440,"status":"ACTIVE"},"anchorInfo":{"qq":"565655","accountPlatformId":"MOBILE","anchorPlatformType":"ENT","ip":"192.168.119.116","sex":"W","mobile":"13922220015","registerType":"MOBILE","userId":30000805,"lastLoginTime":1567737579323,"coverId":1,"createTime":1545966439861,"certName":"发放","mobileStatus":"BIND","loginName":"mobile_30000805","nickname":"萌友30000805","userType":"ANCHOR","certNum":"445101199409061853","lastPlatformType":"PC_WEB","anchorType":"NEW","lastRechargeTime":1563794442981,"lastUpdateTime":1545966468430,"status":"ACTIVE"},"busicode":"USER_REDPACKET","uGameRedpacketDto":{"sendGoodsNum":0,"gmtModified":1568708732000,"sendPrice":0,"lotteryDrawId":48,"awardGoodsId":0,"gmtCreated":1568708732000,"awardTotalPrice":10000,"sendGoodsId":94329,"userId":30000165,"awardPeopleNum":10,"sendGoodsName":"静一静","awardGoodsNum":0,"busiStatus":"START","closeTime":1568708912000,"sendType":"GOODS","startTime":1568708732000,"programId":100165,"awardType":"COIN"}}
     */

    public ContextBean context;

    public static class ContextBean {
        /**
         * userInfo : {"qq":"1663238413","accountPlatformId":"MOBILE","anchorPlatformType":"ENT","ip":"183.129.167.114","sex":"W","mobile":"13222220028","registerType":"MOBILE","userId":30000165,"lastLoginTime":1564021341327,"coverId":1,"createTime":1525232404000,"certName":"发放","mobileStatus":"BIND","loginName":"mobile_30000165","nickname":"26","userType":"ANCHOR","certNum":"445101199409061853","lastPlatformType":"PC_WEB","anchorType":"NEW","lastRechargeTime":1561428727496,"lastUpdateTime":1555407580440,"status":"ACTIVE"}
         * anchorInfo : {"qq":"565655","accountPlatformId":"MOBILE","anchorPlatformType":"ENT","ip":"192.168.119.116","sex":"W","mobile":"13922220015","registerType":"MOBILE","userId":30000805,"lastLoginTime":1567737579323,"coverId":1,"createTime":1545966439861,"certName":"发放","mobileStatus":"BIND","loginName":"mobile_30000805","nickname":"萌友30000805","userType":"ANCHOR","certNum":"445101199409061853","lastPlatformType":"PC_WEB","anchorType":"NEW","lastRechargeTime":1563794442981,"lastUpdateTime":1545966468430,"status":"ACTIVE"}
         * busicode : USER_REDPACKET
         * uGameRedpacketDto : {"sendGoodsNum":0,"gmtModified":1568708732000,"sendPrice":0,"lotteryDrawId":48,"awardGoodsId":0,"gmtCreated":1568708732000,"awardTotalPrice":10000,"sendGoodsId":94329,"userId":30000165,"awardPeopleNum":10,"sendGoodsName":"静一静","awardGoodsNum":0,"busiStatus":"START","closeTime":1568708912000,"sendType":"GOODS","startTime":1568708732000,"programId":100165,"awardType":"COIN"}
         */

        public UserInfoBean userInfo;
        public UserInfoBean anchorInfo;
        public String busicode;
        public UGameRedpacketDtoBean uGameRedpacketDto;

        public static class UserInfoBean {
            /**
             * qq : 1663238413
             * accountPlatformId : MOBILE
             * anchorPlatformType : ENT
             * ip : 183.129.167.114
             * sex : W
             * mobile : 13222220028
             * registerType : MOBILE
             * userId : 30000165
             * lastLoginTime : 1564021341327
             * coverId : 1
             * createTime : 1525232404000
             * certName : 发放
             * mobileStatus : BIND
             * loginName : mobile_30000165
             * nickname : 26
             * userType : ANCHOR
             * certNum : 445101199409061853
             * lastPlatformType : PC_WEB
             * anchorType : NEW
             * lastRechargeTime : 1561428727496
             * lastUpdateTime : 1555407580440
             * status : ACTIVE
             */

            public String qq;
            public String accountPlatformId;
            public String anchorPlatformType;
            public String ip;
            public String sex;
            public String mobile;
            public String registerType;
            public int userId;
            public long lastLoginTime;
            public int coverId;
            public long createTime;
            public String certName;
            public String mobileStatus;
            public String loginName;
            public String nickname;
            public String userType;
            public String certNum;
            public String lastPlatformType;
            public String anchorType;
            public long lastRechargeTime;
            public long lastUpdateTime;
            public String status;
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
    }
}
