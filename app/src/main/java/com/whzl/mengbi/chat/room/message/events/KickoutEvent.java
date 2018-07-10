package com.whzl.mengbi.chat.room.message.events;

import com.whzl.mengbi.chat.room.message.messages.NoChatMsg;

public class KickoutEvent {
    private NoChatMsg noChatMsg;  //禁言的信息

    public KickoutEvent(NoChatMsg noChatMsg) {
        this.noChatMsg = noChatMsg;
    }

    public NoChatMsg getNoChatMsg() {
        return noChatMsg;
    }
}
