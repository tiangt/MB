package com.whzl.mengbi.chat.room.message.events;

import com.whzl.mengbi.chat.room.message.messages.FillHolderMessage;

//更新聊天消息的event
public class UpdatePubChatEvent {
    private FillHolderMessage message;
    public String msgType = "notify"; //聊天(chat)or通知(notify),为了做只显示聊天消息加的功能

    public UpdatePubChatEvent(FillHolderMessage message) {
        this.message = message;
    }

    public UpdatePubChatEvent(FillHolderMessage message, String msgType) {
        this.message = message;
        this.msgType = msgType;
    }

    public FillHolderMessage getMessage() {
        return message;
    }

}
