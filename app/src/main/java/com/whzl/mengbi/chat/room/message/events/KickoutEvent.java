package com.whzl.mengbi.chat.room.message.events;

import com.whzl.mengbi.chat.room.message.messages.NoChatMsg;

public class KickoutEvent {
    private NoChatMsg noChatMsg;  //禁言的信息
    public static final int KICKOUT_CODE = 8; //被提出房间
    public static final int LOGOUT_CODE = 12; //用多个手机打开同一个直播间，强制退出之前的直播间
    public KickoutEvent(NoChatMsg noChatMsg) {
        this.noChatMsg = noChatMsg;
    }

    public NoChatMsg getNoChatMsg() {
        return noChatMsg;
    }
}
