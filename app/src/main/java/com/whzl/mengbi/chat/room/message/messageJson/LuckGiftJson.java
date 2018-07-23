package com.whzl.mengbi.chat.room.message.messageJson;

import java.util.List;


public class LuckGiftJson {
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
        private String nickname;
        private long userId;
        private List<PrizesEntity> prizes;

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public void setPrizes(List<PrizesEntity> prizes) {
            this.prizes = prizes;
        }

        public String getNickname() {
            return nickname;
        }

        public long getUserId() {
            return userId;
        }

        public List<PrizesEntity> getPrizes() {
            return prizes;
        }

        public class PrizesEntity {
            private int times;
            private int giftPrice;
            private int rewardRatio;

            public void setTimes(int times) {
                this.times = times;
            }

            public void setGiftPrice(int giftPrice) {
                this.giftPrice = giftPrice;
            }

            public void setRewardRatio(int rewardRatio) {
                this.rewardRatio = rewardRatio;
            }

            public int getTimes() {
                return times;
            }

            public int getGiftPrice() {
                return giftPrice;
            }

            public int getRewardRatio() {
                return rewardRatio;
            }
        }
    }
}
