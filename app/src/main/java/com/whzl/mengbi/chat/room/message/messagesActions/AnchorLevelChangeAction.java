package com.whzl.mengbi.chat.room.message.messagesActions;

import android.content.Context;

import com.whzl.mengbi.chat.room.message.events.AnchorLevelChangeEvent;
import com.whzl.mengbi.chat.room.message.messageJson.AnchorLevelChangeJson;
import com.whzl.mengbi.util.GsonUtils;

import org.greenrobot.eventbus.EventBus;


/**
 * @author nobody
 */
public class AnchorLevelChangeAction implements Actions {
    @Override
    public void performAction(String msgStr, Context context) {
        AnchorLevelChangeJson anchorLevelChangeJson = GsonUtils.GsonToBean(msgStr, AnchorLevelChangeJson.class);
        if (null == anchorLevelChangeJson || anchorLevelChangeJson.context == null) {
            return;
        }
        EventBus.getDefault().post(new AnchorLevelChangeEvent(anchorLevelChangeJson, context));
    }


}
