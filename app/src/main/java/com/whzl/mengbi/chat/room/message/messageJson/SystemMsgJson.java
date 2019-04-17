package com.whzl.mengbi.chat.room.message.messageJson;

/**
 * @author shaw
 * @date 2018/8/28
 */
public class SystemMsgJson {


    public String eventCode;
    public String msgType;
    public String display;
    public ContextBean context;
    public String type;
    public String platform;

    public static class ContextBean {

        public String display;
        public String userType;
        public String message;
    }

    public static class Message{
        public String message;
        public String pic;
        public String contentLink;
    }
}
