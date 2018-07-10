package com.whzl.mengbi.chat.room.message.messageJson;


public class BroadCastJson {

    /**
     * platform : MOBILE
     * eventCode : BROADCAST
     * context : {"message":"test88","anchorId":"88833","nickname":"吾意独怜才","userId":11284331,"anchorNickname":"zytest300"}
     * msgType : EVENT
     * display : BROADCAST
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
         * message : test88
         * anchorId : 88833
         * nickname : 吾意独怜才
         * userId : 11284331
         * anchorNickname : zytest300
         */
        private String message;
        private String anchorId;
        private String nickname;
        private int userId;
        private String anchorNickname;
        private int programId;

        public int getProgramId() {
            return programId;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public void setAnchorId(String anchorId) {
            this.anchorId = anchorId;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public void setAnchorNickname(String anchorNickname) {
            this.anchorNickname = anchorNickname;
        }

        public String getMessage() {
            return message;
        }

        public String getAnchorId() {
            return anchorId;
        }

        public String getNickname() {
            return nickname;
        }

        public int getUserId() {
            return userId;
        }

        public String getAnchorNickname() {
            return anchorNickname;
        }
    }
}
