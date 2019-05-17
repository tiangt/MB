package com.whzl.mengbi.chat.room.message.messagesActions;

import android.content.Context;

import com.whzl.mengbi.chat.room.message.events.OneKeyOfflineEvent;
import com.whzl.mengbi.chat.room.message.messageJson.OneKeyOfflineJson;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * @author nobody
 * @date 2019/5/17
 */
public class OneKeyOfflineAction implements Actions {
    @Override
    public void performAction(String msgStr, Context context) {
        LogUtils.e("OneKeyOfflineAction  " + msgStr);
        OneKeyOfflineJson oneKeyOfflineJson = GsonUtils.GsonToBean(msgStr, OneKeyOfflineJson.class);
        if (oneKeyOfflineJson == null || oneKeyOfflineJson.context == null || oneKeyOfflineJson.context.sessionList == null) {
            return;
        }
        EventBus.getDefault().post(new OneKeyOfflineEvent(context,oneKeyOfflineJson));
    }
}
