package com.whzl.mengbi.chat.room.message.messagesActions;

import android.content.Context;

import com.whzl.mengbi.chat.room.message.events.UpdatePubChatEvent;
import com.whzl.mengbi.chat.room.message.events.UserRedpacketAwardEvent;
import com.whzl.mengbi.chat.room.message.messageJson.UserRedpacketAwardJson;
import com.whzl.mengbi.chat.room.message.messages.UserRedpacketAwardBroadMessage;
import com.whzl.mengbi.chat.room.message.messages.UserRedpacketAwardMessage;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * @author nobody
 * @date 2019-09-17
 */
public class UserRedpacketAwardBroadAction implements Actions {

    @Override
    public void performAction(String msgStr, Context context) {
        LogUtils.e("UserRedpacketAwardBroadAction  " + msgStr);
        UserRedpacketAwardJson userRedpacketAwardJson = GsonUtils.GsonToBean(msgStr, UserRedpacketAwardJson.class);
        if (userRedpacketAwardJson != null && userRedpacketAwardJson.context != null) {
            //红包抽奖发奖
            EventBus.getDefault().post(new UserRedpacketAwardEvent(context, userRedpacketAwardJson));
//            EventBus.getDefault().post(new UpdatePubChatEvent(new UserRedpacketAwardBroadMessage(context, userRedpacketAwardJson)));
        }
    }
}
