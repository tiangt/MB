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

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    public void setContext(ContextEntity context) {
        this.context = context;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPlatform() {
        return platform;
    }

    public String getEventCode() {
        return eventCode;
    }

    public ContextEntity getContext() {
        return context;
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
         */
        private int goodsId;
        private int count;
        private int comboTimes;
        private int toUserId;
        private String nickname;
        private long dateLong;
        private String color;
        private int userId;
        private int goodsPicId;
        private String toNickname;
        private String goodsName;
        private boolean cacheIt;//是否需要缓存，若不需要则只显示一遍，否则，显示seconds秒
        private long seconds;//跑道显示时间，单位：S
        private String runwayAppend;//跑道寄语

        public void setGoodsId(int goodsId) {
            this.goodsId = goodsId;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public void setComboTimes(int comboTimes) {
            this.comboTimes = comboTimes;
        }

        public void setToUserId(int toUserId) {
            this.toUserId = toUserId;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public void setDateLong(long dateLong) {
            this.dateLong = dateLong;
        }

        public void setColor(String color) {
            this.color = color;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public void setGoodsPicId(int goodsPicId) {
            this.goodsPicId = goodsPicId;
        }

        public void setToNickname(String toNickname) {
            this.toNickname = toNickname;
        }

        public void setGoodsName(String goodsName) {
            this.goodsName = goodsName;
        }

        public int getGoodsId() {
            return goodsId;
        }

        public int getCount() {
            return count;
        }

        public int getComboTimes() {
            return comboTimes;
        }

        public int getToUserId() {
            return toUserId;
        }

        public String getNickname() {
            return nickname;
        }

        public long getDateLong() {
            return dateLong;
        }

        public String getColor() {
            return color;
        }

        public int getUserId() {
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

        public void setCacheIt(boolean cacheIt) {
            this.cacheIt = cacheIt;
        }

        public long getSeconds() {
            return seconds;
        }

        public void setSeconds(long seconds) {
            this.seconds = seconds;
        }

        public String getRunwayAppend() {
            return runwayAppend;
        }

        public void setRunwayAppend(String runwayAppend) {
            this.runwayAppend = runwayAppend;
        }
    }
}
