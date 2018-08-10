package com.whzl.mengbi.chat.room.message.events;

import com.whzl.mengbi.chat.room.message.messages.FillHolderMessage;

//更新聊天消息的event
public class UpdatePubChatEvent {
    private FillHolderMessage message;

    public void setGuard(boolean guard) {
        isGuard = guard;
    }

    private boolean isGuard;

    public UpdatePubChatEvent(FillHolderMessage message) {
        this.message = message;
    }

    public FillHolderMessage getMessage() {
        return message;
    }

    public boolean isGuard() {
        return isGuard;
    }
}
