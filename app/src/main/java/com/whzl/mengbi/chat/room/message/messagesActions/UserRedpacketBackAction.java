package com.whzl.mengbi.chat.room.message.messagesActions;

import android.content.Context;

import com.whzl.mengbi.chat.room.message.events.UpdatePubChatEvent;
import com.whzl.mengbi.chat.room.message.messageJson.UserRedpacketBackJson;
import com.whzl.mengbi.chat.room.message.messages.UserRedpacketBackMessage;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * @author nobody
 * @date 2019-09-25
 */
public class UserRedpacketBackAction implements Actions {
    @Override
    public void performAction(String msgStr, Context context) {
        LogUtils.e("UserRedpacketBackAction  " + msgStr);
        UserRedpacketBackJson userRedpacketBackJson = GsonUtils.GsonToBean(msgStr, UserRedpacketBackJson.class);
        if (userRedpacketBackJson != null && userRedpacketBackJson.context != null) {
            EventBus.getDefault().post(new UpdatePubChatEvent(new UserRedpacketBackMessage(context, userRedpacketBackJson)));        }
    }
}
