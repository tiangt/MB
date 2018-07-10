package com.whzl.mengbi.chat.room.message.events;

import com.whzl.mengbi.chat.room.message.messages.FillHolderMessage;

//更新聊天消息的event
public class UpdatePubChatEvent {
    private FillHolderMessage message;

    public UpdatePubChatEvent(FillHolderMessage message) {
        this.message = message;
    }

    public FillHolderMessage getMessage() {
        return message;
    }
}
