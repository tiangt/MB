package com.whzl.mengbi.chat.room.message.messageJson;

public class SubProgramJson {
    String eventCode;
    String msgType;
    Context context;

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }


    public class Context {
        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getPlatform() {
            return platform;
        }

        public void setPlatform(String platform) {
            this.platform = platform;
        }

        public long getUserId() {
            return userId;
        }

        public int getLevelValue() {
            return levelValue;
        }

        public void setLevelValue(int levelValue) {
            this.levelValue = levelValue;
        }

        String nickname;
        String platform;
        long userId;
        int levelValue;
    }
}
