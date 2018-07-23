package com.whzl.mengbi.chat.room.message.messagesActions;

import android.content.Context;
import android.util.Log;

import com.whzl.mengbi.chat.room.message.events.UpdatePubChatEvent;
import com.whzl.mengbi.chat.room.message.messageJson.SetManagerJson;
import com.whzl.mengbi.chat.room.message.messages.SetManagerMessage;
import com.whzl.mengbi.util.GsonUtils;

import org.greenrobot.eventbus.EventBus;

public class SetManagerAction implements Actions {
    @Override
    public void performAction(String msgStr, Context context) {
        SetManagerJson setManagerJson = GsonUtils.GsonToBean(msgStr, SetManagerJson.class);
        if(setManagerJson == null){
            Log.e("manager","set manager message error");
            return;
        }
        SetManagerMessage message = new SetManagerMessage(setManagerJson, context);
        EventBus.getDefault().post(new UpdatePubChatEvent(message));
    }
}
