package com.whzl.mengbi.chat.room.message.messagesActions;

import android.content.Context;

import com.whzl.mengbi.chat.room.message.events.StartPlayEvent;
import com.whzl.mengbi.chat.room.message.messageJson.StartStopLiveJson;
import com.whzl.mengbi.util.GsonUtils;

import org.greenrobot.eventbus.EventBus;


public class StartPlayAction implements Actions {

    @Override
    public void performAction(String msgStr, Context context) {
        StartStopLiveJson startStopLiveJson = GsonUtils.GsonToBean(msgStr, StartStopLiveJson.class);
        if (null == startStopLiveJson) {
            return;
        }
        EventBus.getDefault().post(new StartPlayEvent(startStopLiveJson, context));
    }
}
