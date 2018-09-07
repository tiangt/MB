package com.whzl.mengbi.chat.room.message.messagesActions;

import android.content.Context;

import com.whzl.mengbi.chat.room.message.events.UpdatePubChatEvent;
import com.whzl.mengbi.chat.room.message.messageJson.SystemMsgJson;
import com.whzl.mengbi.chat.room.message.messages.SystemMessage;
import com.whzl.mengbi.util.GsonUtils;
import org.greenrobot.eventbus.EventBus;

/**
 * @author shaw
 * @date 2018/8/28
 */
public class SystemMsgAction implements Actions {
    @Override
    public void performAction(String msgStr, Context context) {
        SystemMsgJson systemMsgJson = GsonUtils.GsonToBean(msgStr, SystemMsgJson.class);
        if (null == systemMsgJson || systemMsgJson.context == null || systemMsgJson.context.message == null) {
            return;
        }
        EventBus.getDefault().post(new UpdatePubChatEvent(new SystemMessage(systemMsgJson, context)));
    }


}
