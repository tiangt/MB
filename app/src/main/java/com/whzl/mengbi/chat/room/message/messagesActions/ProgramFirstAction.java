package com.whzl.mengbi.chat.room.message.messagesActions;

import android.content.Context;
import android.util.EventLog;

import com.whzl.mengbi.chat.room.message.events.ProgramFirstNotifyEvent;
import com.whzl.mengbi.chat.room.message.events.UpdatePubChatEvent;
import com.whzl.mengbi.chat.room.message.messageJson.ProgramFirstNotifyJson;
import com.whzl.mengbi.chat.room.message.messages.ProgramFirstMessage;
import com.whzl.mengbi.chat.room.util.ChatRoomInfo;
import com.whzl.mengbi.util.GsonUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * author: yaobo wu
 * date:   On 2018/8/3
 */
public class ProgramFirstAction implements Actions {
    @Override
    public void performAction(String msgStr, Context context) {
        ProgramFirstNotifyJson notifyJson = GsonUtils.GsonToBean(msgStr, ProgramFirstNotifyJson.class);
        if (notifyJson == null||notifyJson.getContext()==null||notifyJson.getContext().getUserId()==null) {
            return;
        }
        try {
            long userId = Long.parseLong(notifyJson.getContext().getUserId());
            ChatRoomInfo.getInstance().setProgramFirstId(userId);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        EventBus.getDefault().post(new UpdatePubChatEvent(new ProgramFirstMessage(notifyJson, context)));
    }
}
