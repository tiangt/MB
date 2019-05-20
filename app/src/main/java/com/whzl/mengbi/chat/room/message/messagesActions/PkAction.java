package com.whzl.mengbi.chat.room.message.messagesActions;

import android.content.Context;

import com.whzl.mengbi.chat.room.message.events.PkEvent;
import com.whzl.mengbi.chat.room.message.events.UpdatePubChatEvent;
import com.whzl.mengbi.chat.room.message.messageJson.PkJson;
import com.whzl.mengbi.chat.room.message.messages.PkMessage;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;

import org.greenrobot.eventbus.EventBus;


/**
 * @author nobody
 */
public class PkAction implements Actions {
    @Override
    public void performAction(String msgStr, Context context) {
        LogUtils.e2("pkAction " + msgStr);
        PkJson pkJson = GsonUtils.GsonToBean(msgStr, PkJson.class);
        if (null == pkJson || pkJson.context == null) {
            return;
        }
        EventBus.getDefault().post(new PkEvent(pkJson, context));

        if ("PK_FIRST_BLOOD".equals(pkJson.context.busiCode) || "PK_RECORD".equals(pkJson.context.busiCode)
                || "BALCK_HOUSE".equals(pkJson.context.busiCode) || "uRescueAnchor".equals(pkJson.context.busiCode)
                || "PK_OPEN_EXP_CARD".equals(pkJson.context.busiCode)) {
            PkMessage pkMessage = new PkMessage(pkJson, context);
            UpdatePubChatEvent chatEvent = new UpdatePubChatEvent(pkMessage);
            EventBus.getDefault().post(chatEvent);
        }
    }
}
