package com.whzl.mengbi.chat.room.message.messageJson;

public class RunWayJson {

    /**
     * platform : MOBILE
     * eventCode : RUNWAY
     * context : {"goodsId":91141,"count":500,"comboTimes":1,"toUserId":15069536,"nickname":"ceshi0001","dateLong":1433902856344,"color":"gift_02","userId":14757850,"goodsPicId":1386,"toNickname":"QichengTest_4","goodsName":"护士帽"}
     * msgType : EVENT
     * display : RUNWAY
     * type : busi.msg
     */
    private String platform;
    private String eventCode;
    private ContextEntity context;
    private String msgType;
    private String display;
    private String type;

    public String getPlatform() {
        return platform;
    }

    public String getEventCode() {
        return eventCode;
    }

    public ContextEntity getContext() {
        return context;
    }

    public void setContext(ContextEntity context) {
        this.context = context;
    }

    public String getMsgType() {
        return msgType;
    }

    public String getDisplay() {
        return display;
    }

    public String getType() {
        return type;
    }

    public class ContextEntity {
        /**
         * goodsId : 91141
         * count : 500
         * comboTimes : 1
         * toUserId : 15069536
         * nickname : ceshi0001
         * dateLong : 1433902856344
         * color : gift_02
         * userId : 14757850
         * goodsPicId : 1386
         * toNickname : QichengTest_4
         * goodsName : 护士帽
         * runwayType ：跑道类型（destory 攻占标志、getOn 登上标志）
         */
        private int goodsId;
        private int count;
        private int comboTimes;
        private long toUserId;
        private String nickname;
        private long dateLong;
        private long userId;
        private int goodsPicId;
        private String toNickname;
        private String goodsName;
        private String runWayType;

        public String getRunWayType() {
            return runWayType;
        }

        public int getProgramId() {
            return programId;
        }

        private int programId;

        public String getGoodsPic() {
            return goodsPic;
        }

        private String goodsPic;

        private boolean cacheIt;//是否需要缓存，若不需要则只显示一遍，否则，显示seconds秒
        private long seconds;//跑道显示时间，单位：S
        private int price;

        public int getGoodsId() {
            return goodsId;
        }

        public int getCount() {
            return count;
        }

        public int getComboTimes() {
            return comboTimes;
        }

        public long getToUserId() {
            return toUserId;
        }

        public String getNickname() {
            return nickname;
        }

        public long getDateLong() {
            return dateLong;
        }

        public long getUserId() {
            return userId;
        }

        public int getGoodsPicId() {
            return goodsPicId;
        }

        public String getToNickname() {
            return toNickname;
        }

        public String getGoodsName() {
            return goodsName;
        }

        public boolean isCacheIt() {
            return cacheIt;
        }

        public long getSeconds() {
            return seconds;
        }

        public int getPrice() {
            return price;
        }
    }
}
