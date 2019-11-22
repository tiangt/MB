package com.whzl.mengbi.chat.room.message.messagesActions;

import android.content.Context;

import com.whzl.mengbi.chat.room.message.events.RobBigLuckyEvent;
import com.whzl.mengbi.chat.room.message.messageJson.RobBigLuckyJson;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * @author nobody
 * @date 2019-11-22
 */
public class RobBigLuckyAction implements Actions {
    @Override
    public void performAction(String msgStr, Context context) {
        LogUtils.e("RobBigLuckyAction  " + msgStr);
        RobBigLuckyJson robBigLuckyJson = GsonUtils.GsonToBean(msgStr, RobBigLuckyJson.class);
        if (robBigLuckyJson != null && robBigLuckyJson.context != null) {
            EventBus.getDefault().post(new RobBigLuckyEvent(context, robBigLuckyJson));
        }
    }
}
