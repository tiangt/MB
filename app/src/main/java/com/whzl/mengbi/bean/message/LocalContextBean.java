package com.whzl.mengbi.bean.message;

public class LocalContextBean {

    private ContextBean context;

    public ContextBean getContext() {
        return context;
    }

    public void setContext(ContextBean context) {
        this.context = context;
    }

    public static class ContextBean {

        private InfoBean info;
        private String display;
        private String message;
        private String userType;


        public InfoBean getInfo() {
            return info;
        }

        public void setInfo(InfoBean info) {
            this.info = info;
        }

        public String getDisplay() {
            return display;
        }

        public void setDisplay(String display) {
            this.display = display;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public static class InfoBean {

            private String nickname;
            private int userId;

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
}
