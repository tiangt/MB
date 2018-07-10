package com.whzl.mengbi.chat.room.message.messageJson;

import java.util.List;

public class AnimJson {

    /**
     * resources : [{"animationResId":141,"resValue":"2293","resType":"PLIST"},{"animationResId":133,"resValue":"2284","resType":"PNG"}]
     * platform : MOBILE
     * context : {"goodsId":91104,"count":1,"comboTimes":0,"toUserId":15070491,"nickname":"我不是汽水","dateLong":1435901700694,"color":"default","userId":16070728,"goodsPicId":329,"toNickname":"zytest300","goodsName":"直升机"}
     * msgType : ANIMATION
     * display : CENTER
     * animName : 直升机礼物
     * type : busi.msg
     * animType : PNG
     * animId : 76
     */
    private List<ResourcesEntity> resources;
    private String platform;
    private ContextEntity context;
    private String msgType;
    private String display;
    private String animName;
    private String type;
    private String animType;
    private int animId;

    public void setResources(List<ResourcesEntity> resources) {
        this.resources = resources;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
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

    public void setAnimName(String animName) {
        this.animName = animName;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setAnimType(String animType) {
        this.animType = animType;
    }

    public void setAnimId(int animId) {
        this.animId = animId;
    }

    public List<ResourcesEntity> getResources() {
        return resources;
    }

    public String getPlatform() {
        return platform;
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

    public String getAnimName() {
        return animName;
    }

    public String getType() {
        return type;
    }

    public String getAnimType() {
        return animType;
    }

    public int getAnimId() {
        return animId;
    }

    public class ResourcesEntity {
        /**
         * animationResId : 141
         * resValue : 2293
         * resType : GIF
         */
        private int animationResId;
        private String location;
        private String resValue;
        private String resType;

        public void setAnimationResId(int animationResId) {
            this.animationResId = animationResId;
        }

        public void setLocation(String location) {this.location = location;}

        public String getLocation() {return this.location;}

        public void setResValue(String resValue) {
            this.resValue = resValue;
        }

        public void setResType(String resType) {
            this.resType = resType;
        }

        public int getAnimationResId() {
            return animationResId;
        }

        public String getResValue() {
            return resValue;
        }

        public String getResType() {
            return resType;
        }
    }

    public class ContextEntity {
        /**
         * goodsId : 91104
         * count : 1
         * comboTimes : 0
         * toUserId : 15070491
         * nickname : 大王叫我巡山
         * dateLong : 1435901700694
         * color : default
         * userId : 16070728
         * goodsPicId : 329
         * toNickname : zytest300
         * goodsName : 直升机
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
    }
}
