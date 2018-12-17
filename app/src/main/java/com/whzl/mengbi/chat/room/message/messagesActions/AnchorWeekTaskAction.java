package com.whzl.mengbi.chat.room.message.messagesActions;

import android.content.Context;

import com.whzl.mengbi.chat.room.message.events.AnchorWeekTaskEvent;
import com.whzl.mengbi.chat.room.message.messageJson.AnchorWeekTaskJson;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * @author nobody
 * @date 2018/12/13
 */
public class AnchorWeekTaskAction implements Actions {
    @Override
    public void performAction(String msgStr, Context context) {
        LogUtils.e("ssssssssssss  " + msgStr);
        AnchorWeekTaskJson anchorWeekTaskJson = GsonUtils.GsonToBean(msgStr, AnchorWeekTaskJson.class);
        if (anchorWeekTaskJson != null && anchorWeekTaskJson.context != null) {
            EventBus.getDefault().post(new AnchorWeekTaskEvent(context, anchorWeekTaskJson));
        }
    }
}
