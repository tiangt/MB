package com.whzl.mengbi.chat.room.message.messagesActions;

import android.content.Context;

import com.whzl.mengbi.chat.room.message.events.LuckGiftEvent;
import com.whzl.mengbi.chat.room.message.events.UpdatePubChatEvent;
import com.whzl.mengbi.chat.room.message.messageJson.LuckGiftJson;
import com.whzl.mengbi.chat.room.message.messages.LuckGiftMessage;
import com.whzl.mengbi.util.GsonUtils;

import org.greenrobot.eventbus.EventBus;


public class LuckGiftAction implements Actions {
    @Override
    public void performAction(String msgStr, Context context) {
        LuckGiftJson luckGiftJson = GsonUtils.GsonToBean(msgStr, LuckGiftJson.class);
        if (null == luckGiftJson) {
            return;
        }
        EventBus.getDefault().post(new LuckGiftEvent(luckGiftJson));
        EventBus.getDefault().post(new UpdatePubChatEvent(new LuckGiftMessage(luckGiftJson, context)));
    }
}
