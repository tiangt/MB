package com.whzl.mengbi.chat.room.message.messagesActions;

import android.content.Context;

import com.whzl.mengbi.chat.room.message.events.UserRedpacketBroadEvent;
import com.whzl.mengbi.chat.room.message.messageJson.UserRedpacketJson;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * @author nobody
 * @date 2019-09-17
 */
public class UserRedpacketBroadAction implements Actions {

    @Override
    public void performAction(String msgStr, Context context) {
        LogUtils.e("UserRedpacketBroadAction  " + msgStr);
        UserRedpacketJson userRedpacketJson = GsonUtils.GsonToBean(msgStr, UserRedpacketJson.class);
        if (userRedpacketJson != null && userRedpacketJson.context != null && userRedpacketJson.context.anchorInfo != null
                && userRedpacketJson.context.userInfo != null) {
            //发起红包抽奖
            EventBus.getDefault().post(new UserRedpacketBroadEvent(context,userRedpacketJson));
        }
    }
}