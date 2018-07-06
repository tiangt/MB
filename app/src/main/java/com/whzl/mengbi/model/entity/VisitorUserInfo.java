package com.whzl.mengbi.model.entity;

public class VisitorUserInfo extends ResponseInfo {

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String sessionId;
        private int userId;
        private String nickname;

        public String getSessionId() {
            return sessionId;
        }

        public void setSessionId(String sessionId) {
            this.sessionId = sessionId;
        }

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }
    }

    @Override
    public String toString() {
        return "VisitorUserInfo{" +
                ", data=" + data +
                '}';
    }
}
