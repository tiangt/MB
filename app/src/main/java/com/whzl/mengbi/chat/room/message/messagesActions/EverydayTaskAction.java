package com.whzl.mengbi.chat.room.message.messagesActions;

import android.content.Context;

import com.whzl.mengbi.chat.room.message.events.EverydayEvent;
import com.whzl.mengbi.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * @author nobody
 * @date 2018/12/7
 */
public class EverydayTaskAction implements Actions {
    @Override
    public void performAction(String msgStr, Context context) {
        LogUtils.e("ssssssssssss  "+msgStr);
        EventBus.getDefault().post(new EverydayEvent());
    }
}
