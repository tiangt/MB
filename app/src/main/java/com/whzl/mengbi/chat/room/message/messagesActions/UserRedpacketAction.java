package com.whzl.mengbi.chat.room.message.messagesActions;

import android.content.Context;

import com.whzl.mengbi.chat.room.message.events.UpdatePubChatEvent;
import com.whzl.mengbi.chat.room.message.events.UserRedpacketEvent;
import com.whzl.mengbi.chat.room.message.messageJson.UserRedpacketJson;
import com.whzl.mengbi.chat.room.message.messages.UserRedpacketMessage;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * @author nobody
 * @date 2019-09-17
 */
public class UserRedpacketAction implements Actions {

    @Override
    public void performAction(String msgStr, Context context) {
        LogUtils.e("UserRedpacketAction  " + msgStr);
        UserRedpacketJson userRedpacketJson = GsonUtils.GsonToBean(msgStr, UserRedpacketJson.class);
        if (userRedpacketJson != null && userRedpacketJson.context != null && userRedpacketJson.context.anchorInfo != null
                && userRedpacketJson.context.userInfo != null) {
            //发起红包抽奖
            EventBus.getDefault().post(new UserRedpacketEvent());
            EventBus.getDefault().post(new UpdatePubChatEvent(new UserRedpacketMessage(context, userRedpacketJson)));
        }
    }
}
