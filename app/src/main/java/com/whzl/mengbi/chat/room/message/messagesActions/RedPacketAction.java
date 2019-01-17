package com.whzl.mengbi.chat.room.message.messagesActions;

import android.content.Context;

import com.whzl.mengbi.chat.room.message.events.RedPackTreasureEvent;
import com.whzl.mengbi.chat.room.message.events.UpdatePubChatEvent;
import com.whzl.mengbi.chat.room.message.messageJson.RedPackJson;
import com.whzl.mengbi.chat.room.message.messages.RedPackMessage;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * @author nobody
 * @date 2019/1/16
 */
public class RedPacketAction implements Actions {
    @Override
    public void performAction(String msgStr, Context context) {
        LogUtils.e("RedPacketAction  " + msgStr);
        RedPackJson redPackJson = GsonUtils.GsonToBean(msgStr, RedPackJson.class);
        if (null == redPackJson || redPackJson.context == null) {
            return;
        }
        RedPackMessage message = new RedPackMessage(context, redPackJson);
        if (!redPackJson.context.busiCodeName.equals("RP_RETURN_TO_PT") && !redPackJson.context.busiCodeName.equals("RP_HAD_FINISHED")) {
            EventBus.getDefault().post(new UpdatePubChatEvent(message));
        }
        EventBus.getDefault().post(new RedPackTreasureEvent(redPackJson));
    }
}
