package com.whzl.mengbi.chat.room.message.messageJson;


public class GiftJson {
    public class GiftContext{
        int count;
        String goodsName;
        String nickname;
        String toNickname;
        int toUserId;
        int userId;
        int goodsPicId;
        int goodsId;
        long lastUpdateTime;

        public long getLastUpdateTime() {
            return lastUpdateTime;
        }

        public void setLastUpdateTime(long lastUpdateTime) {
            this.lastUpdateTime = lastUpdateTime;
        }

        public int getLevelValue() {
            return levelValue;
        }

        public void setLevelValue(int levelValue) {
            this.levelValue = levelValue;
        }

        int levelValue;

        public int getGoodsId() {
            return goodsId;
        }

        public int getGoodsPicId() {
            return goodsPicId;
        }

        public int getCount() {
            return count;
        }

        public String getGoodsName() {
            return goodsName;
        }

        public String getNickname() {
            return nickname;
        }

        public String getToNickname() {
            return toNickname;
        }

        public int getToUserId() {
            return toUserId;
        }

        public int getUserId() {
            return userId;
        }
    }

    String eventCode;
    GiftContext context;

    public String getEventCode() {
        return eventCode;
    }

    public GiftContext getContext() {
        return context;
    }
}
