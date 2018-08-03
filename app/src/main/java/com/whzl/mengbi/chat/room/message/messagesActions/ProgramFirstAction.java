package com.whzl.mengbi.chat.room.message.messagesActions;

import android.content.Context;

import com.whzl.mengbi.chat.room.message.events.ProgramFirstNotifyEvent;
import com.whzl.mengbi.chat.room.message.messageJson.ProgramFirstNotifyJson;
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
        if (notifyJson == null) {
            return;
        }
        EventBus.getDefault().post(new ProgramFirstNotifyEvent(notifyJson, context));
    }
}
