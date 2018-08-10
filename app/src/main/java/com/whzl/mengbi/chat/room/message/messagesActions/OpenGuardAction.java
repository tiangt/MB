package com.whzl.mengbi.chat.room.message.messagesActions;

import android.content.Context;

import com.whzl.mengbi.chat.room.message.events.UpdatePubChatEvent;
import com.whzl.mengbi.chat.room.message.messageJson.OpenGuardJson;
import com.whzl.mengbi.chat.room.message.messages.OpenGuardMessage;
import com.whzl.mengbi.util.GsonUtils;

import org.greenrobot.eventbus.EventBus;

public class OpenGuardAction implements Actions {
    @Override
    public void performAction(String msgStr, Context context) {
        OpenGuardJson openGuardJson = GsonUtils.GsonToBean(msgStr, OpenGuardJson.class);
        if (null == openGuardJson) {
            return;
        }
        OpenGuardMessage openGuardMessage = new OpenGuardMessage(openGuardJson, context);
        UpdatePubChatEvent updatePubChatEvent = new UpdatePubChatEvent(openGuardMessage);
        updatePubChatEvent.setGuard(true);
        EventBus.getDefault().post(updatePubChatEvent);
    }
}
