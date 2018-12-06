package com.whzl.mengbi.chat.room.message.messagesActions;

import android.content.Context;

import com.whzl.mengbi.chat.room.message.events.WeekStarEvent;
import com.whzl.mengbi.chat.room.message.messageJson.WeekStarJson;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * @author nobody
 * @date 2018/12/3
 */
public class WeekStarAction implements Actions {
    @Override
    public void performAction(String msgStr, Context context) {
        LogUtils.e("sssssssss   "+msgStr);
        WeekStarJson weekStarJson = GsonUtils.GsonToBean(msgStr, WeekStarJson.class);
        if (null == weekStarJson || weekStarJson.context == null) {
            return;
        }
        EventBus.getDefault().post(new WeekStarEvent(context,weekStarJson));
    }
}
