package com.whzl.mengbi.chat.room.message.messagesActions;

import android.content.Context;

import com.whzl.mengbi.chat.room.message.events.UpdatePubChatEvent;
import com.whzl.mengbi.chat.room.message.messageJson.SubProgramJson;
import com.whzl.mengbi.chat.room.message.messages.SubProgramMsg;
import com.whzl.mengbi.util.GsonUtils;

import org.greenrobot.eventbus.EventBus;

public class SubProgramAction implements Actions {
    @Override
    public void performAction(String msgStr, Context context) {
        SubProgramJson subProJson = GsonUtils.GsonToBean(msgStr, SubProgramJson.class);
        if (null == subProJson) {
            return;
        }
        SubProgramMsg subProgramMsg = new SubProgramMsg(subProJson, context);
        UpdatePubChatEvent subEvent = new UpdatePubChatEvent(subProgramMsg);
        EventBus.getDefault().post(subEvent);
    }
}
