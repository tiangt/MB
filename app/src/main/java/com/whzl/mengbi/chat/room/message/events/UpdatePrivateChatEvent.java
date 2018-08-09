package com.whzl.mengbi.chat.room.message.events;

import com.whzl.mengbi.chat.room.message.messages.FillHolderMessage;

//更新聊天消息的event
public class UpdatePrivateChatEvent {
    private FillHolderMessage message;

    public UpdatePrivateChatEvent(FillHolderMessage message) {
        this.message = message;
    }

    public FillHolderMessage getMessage() {
        return message;
    }
}
