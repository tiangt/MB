package com.whzl.mengbi.chat.room.message.messagesActions;

import android.content.Context;

import com.whzl.mengbi.chat.room.message.events.RedPackTreasureEvent;
import com.whzl.mengbi.chat.room.message.events.UpdatePubChatEvent;
import com.whzl.mengbi.chat.room.message.messageJson.RedPackJson;
import com.whzl.mengbi.chat.room.message.messages.RedPackPoolMessage;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * @author nobody
 * @date 2019/5/20
 */
public class RedPacketPoolAction implements Actions {
    @Override
    public void performAction(String msgStr, Context context) {
        LogUtils.e("RedPacketPoolAction  " + msgStr);
        RedPackJson redPackJson = GsonUtils.GsonToBean(msgStr, RedPackJson.class);
        if (redPackJson == null || redPackJson.context == null) {
            return;
        }
        RedPackPoolMessage message = new RedPackPoolMessage(context, redPackJson);
        EventBus.getDefault().post(new UpdatePubChatEvent(message));
        EventBus.getDefault().post(new RedPackTreasureEvent(redPackJson));
    }
}
