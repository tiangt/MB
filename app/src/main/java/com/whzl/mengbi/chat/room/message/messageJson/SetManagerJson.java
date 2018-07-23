package com.whzl.mengbi.chat.room.message.messageJson;

public class SetManagerJson {

    /**
     * platform : MOBILE
     * eventCode : SET_MANAGER
     * context : {"toUserId":16070732,"nickname":"zytest300","userId":15070491,"toUserNickname":"哪个是汽水"}
     * msgType : EVENT
     * type : busi.msg
     */
    private String platform;
    private String eventCode;
    private ContextEntity context;
    private String msgType;
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

    public String getType() {
        return type;
    }

    public class ContextEntity {
        /**
         * toUserId : 16070732
         * nickname : zytest300
         * userId : 15070491
         * toUserNickname : 哪个是汽水
         */
        private long toUserId;
        private String nickname;
        private long userId;
        private String toUserNickname;

        public void setToUserId(int toUserId) {
            this.toUserId = toUserId;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public void setToUserNickname(String toUserNickname) {
            this.toUserNickname = toUserNickname;
        }

        public long getToUserId() {
            return toUserId;
        }

        public String getNickname() {
            return nickname;
        }

        public long getUserId() {
            return userId;
        }

        public String getToUserNickname() {
            return toUserNickname;
        }
    }
}
