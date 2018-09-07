package com.whzl.mengbi.model.entity;

public class RegisterInfo extends ResponseInfo {


    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {

        private String sessionId;
        private long userId;
        private String nickname;
        private String avatar;
        private String userType;
        private String lastRechargeTime;

        public String getSessionId() {
            return sessionId;
        }

        public void setSessionId(String sessionId) {
            this.sessionId = sessionId;
        }

        public long getUserId() {
            return userId;
        }

        public String getNickName() {
            return nickname;
        }

        public void setNickName(String nickName) {
            this.nickname = nickName;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public String getLastRechargeTime() {
            return lastRechargeTime;
        }

        public void setLastRechargeTime(String lastRechargeTime) {
            this.lastRechargeTime = lastRechargeTime;
        }
    }
}
