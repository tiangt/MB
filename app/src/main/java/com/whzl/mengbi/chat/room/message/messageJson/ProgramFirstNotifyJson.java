package com.whzl.mengbi.chat.room.message.messageJson;

/**
 * author: yaobo wu
 * date:   On 2018/8/3
 */
public class ProgramFirstNotifyJson {
    String eventCode;
    ContextEntity context;

    public String getEventCode() {
        return eventCode;
    }

    public ContextEntity getContext() {
        return context;
    }

    public class ContextEntity{
        String nickName;
        String userId;

        public String getNickName() {
            return nickName;
        }

        public String getUserId() {
            return userId;
        }
    }
}
