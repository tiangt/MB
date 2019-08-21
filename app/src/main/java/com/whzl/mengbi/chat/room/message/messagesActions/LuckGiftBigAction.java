package com.whzl.mengbi.chat.room.message.messagesActions;

import android.content.Context;

import com.whzl.mengbi.chat.room.message.events.LuckGiftBigEvent;
import com.whzl.mengbi.chat.room.message.events.UpdatePubChatEvent;
import com.whzl.mengbi.chat.room.message.messageJson.LuckGiftBigJson;
import com.whzl.mengbi.chat.room.message.messages.LuckGiftMessage;
import com.whzl.mengbi.util.GsonUtils;

import org.greenrobot.eventbus.EventBus;


/**
 * @author nobody
 */
public class LuckGiftBigAction implements Actions {
    @Override
    public void performAction(String msgStr, Context context) {
        LuckGiftBigJson luckGiftBigJson = GsonUtils.GsonToBean(msgStr, LuckGiftBigJson.class);
        if (null == luckGiftBigJson || luckGiftBigJson.context == null) {
            return;
        }
//        EventBus.getDefault().post(new LuckGiftBigEvent(luckGiftBigJson, context));
        EventBus.getDefault().post(new UpdatePubChatEvent(new LuckGiftMessage(luckGiftBigJson, context)));
    }


}
