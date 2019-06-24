package com.whzl.mengbi.chat.room.message.messagesActions;

import android.content.Context;

import com.whzl.mengbi.chat.room.message.events.FlopCardEvent;
import com.whzl.mengbi.chat.room.message.events.UpdatePubChatEvent;
import com.whzl.mengbi.chat.room.message.messageJson.FlopCardJson;
import com.whzl.mengbi.chat.room.message.messages.FlopCardMessage;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * @author nobody
 * @date 2019/5/30
 */
public class FlopCardAction implements Actions {
    @Override
    public void performAction(String msgStr, Context context) {
        LogUtils.e("FlopCardAction  " + msgStr);
        FlopCardJson flopCardJson = GsonUtils.GsonToBean(msgStr, FlopCardJson.class);
        if (flopCardJson == null || flopCardJson.context == null) {
            return;
        }
        EventBus.getDefault().post(new UpdatePubChatEvent(new FlopCardMessage(flopCardJson, context)));

        EventBus.getDefault().post(new FlopCardEvent(flopCardJson, context));
    }
}
