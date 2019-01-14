package com.whzl.mengbi.chat.room.message.messagesActions;

import android.content.Context;

import com.whzl.mengbi.chat.room.message.events.PlayNotifyEvent;
import com.whzl.mengbi.chat.room.message.messageJson.PlayNotifyJson;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * @author nobody
 * @date 2019/1/14
 */
public class PlayNotifyAction implements Actions {
    @Override
    public void performAction(String msgStr, Context context) {
        LogUtils.e("PlayNotifyAction  " + msgStr);
        PlayNotifyJson playNotifyJson = GsonUtils.GsonToBean(msgStr, PlayNotifyJson.class);
        if (null == playNotifyJson || playNotifyJson.context == null) {
            return;
        }
        EventBus.getDefault().post(new PlayNotifyEvent(context, playNotifyJson));
    }
}
