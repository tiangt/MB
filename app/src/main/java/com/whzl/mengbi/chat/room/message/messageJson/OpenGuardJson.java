package com.whzl.mengbi.chat.room.message.messageJson;

public class OpenGuardJson {
    private String eventCode;
    private ContextEntity context;

    public String getEventCode() {
        return eventCode;
    }

    public ContextEntity getContext() {
        return context;
    }

    public class ContextEntity{
        String nickname;
        int userId;

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }
    }
}
