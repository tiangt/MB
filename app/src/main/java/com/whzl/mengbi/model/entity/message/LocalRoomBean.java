package com.whzl.mengbi.model.entity.message;

public class LocalRoomBean {

    private String display;
    private String eventCode;
    private String msgType;
    private String platform;
    private String type;

    private LocalContextBean localContextBean;

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

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

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public LocalContextBean getLocalContextBean() {
        return localContextBean;
    }

    public void setLocalContextBean(LocalContextBean localContextBean) {
        this.localContextBean = localContextBean;
    }
}
