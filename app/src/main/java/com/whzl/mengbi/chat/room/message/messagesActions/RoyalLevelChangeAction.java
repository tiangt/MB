package com.whzl.mengbi.chat.room.message.messagesActions;

import android.content.Context;

import com.whzl.mengbi.chat.room.message.events.RoyalLevelChangeEvent;
import com.whzl.mengbi.chat.room.message.messageJson.RoyalLevelChangeJson;
import com.whzl.mengbi.util.GsonUtils;

import org.greenrobot.eventbus.EventBus;


/**
 * @author nobody
 */
public class RoyalLevelChangeAction implements Actions {
    @Override
    public void performAction(String msgStr, Context context) {
        RoyalLevelChangeJson royalLevelChangeJson = GsonUtils.GsonToBean(msgStr, RoyalLevelChangeJson.class);
        if (null == royalLevelChangeJson || royalLevelChangeJson.context == null) {
            return;
        }
        EventBus.getDefault().post(new RoyalLevelChangeEvent(royalLevelChangeJson, context));
    }


}
