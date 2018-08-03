package com.whzl.mengbi.chat.room.message.messagesActions;

import android.content.Context;

import com.whzl.mengbi.chat.room.message.events.UpdateProgramEvent;
import com.whzl.mengbi.chat.room.message.messageJson.UpdateProgramJson;
import com.whzl.mengbi.util.GsonUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * author: yaobo wu
 * date:   On 2018/8/3
 */
public class UpdateProgramAction implements Actions {
    @Override
    public void performAction(String msgStr, Context context) {
        UpdateProgramJson programJson = GsonUtils.GsonToBean(msgStr, UpdateProgramJson.class);
        if (null == programJson) {
            return;
        }
        EventBus.getDefault().post(new UpdateProgramEvent(programJson, context));
    }
}
