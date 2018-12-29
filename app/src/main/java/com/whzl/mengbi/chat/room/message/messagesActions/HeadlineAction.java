package com.whzl.mengbi.chat.room.message.messagesActions;


import android.content.Context;

import com.whzl.mengbi.chat.room.message.events.HeadLineEvent;
import com.whzl.mengbi.chat.room.message.messageJson.HeadLineJson;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * @author cliang
 * @date 2018.12.27
 */
public class HeadlineAction implements Actions {
    @Override
    public void performAction(String msgStr, Context context) {
        LogUtils.e("HeadlineAction  " + msgStr);
        HeadLineJson headLineJson = GsonUtils.GsonToBean(msgStr, HeadLineJson.class);
        if (null == headLineJson || headLineJson.context == null) {
            return;
        }
        EventBus.getDefault().post(new HeadLineEvent(context, headLineJson));
    }
}
