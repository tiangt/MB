package com.whzl.mengbi.chat.room.message.messagesActions;

import android.content.Context;

import com.whzl.mengbi.chat.room.message.events.UserLevelChangeEvent;
import com.whzl.mengbi.chat.room.message.messageJson.UserLevelChangeJson;
import com.whzl.mengbi.util.GsonUtils;

import org.greenrobot.eventbus.EventBus;


/**
 * @author nobody
 */
public class UserLevelChangeAction implements Actions {
    @Override
    public void performAction(String msgStr, Context context) {
        UserLevelChangeJson userLevelChangeJson = GsonUtils.GsonToBean(msgStr, UserLevelChangeJson.class);
        if (null == userLevelChangeJson || userLevelChangeJson.context == null) {
            return;
        }
        EventBus.getDefault().post(new UserLevelChangeEvent(userLevelChangeJson, context));
    }


}
