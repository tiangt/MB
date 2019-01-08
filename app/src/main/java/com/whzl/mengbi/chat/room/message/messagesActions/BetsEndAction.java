package com.whzl.mengbi.chat.room.message.messagesActions;

import android.content.Context;

import com.whzl.mengbi.chat.room.message.events.BetsEndEvent;
import com.whzl.mengbi.chat.room.message.events.UpdatePubChatEvent;
import com.whzl.mengbi.chat.room.message.messageJson.BetsEndJson;
import com.whzl.mengbi.chat.room.message.messages.BetsEndMessage;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * @author nobody
 * @date 2019/1/8
 */
public class BetsEndAction implements Actions {
    @Override
    public void performAction(String msgStr, Context context) {
        LogUtils.e("BetsEndAction  " + msgStr);
        BetsEndJson betsEndJson = GsonUtils.GsonToBean(msgStr, BetsEndJson.class);
        if (null == betsEndJson || betsEndJson.context == null) {
            return;
        }
        BetsEndMessage message = new BetsEndMessage(betsEndJson, context);
        EventBus.getDefault().post(new UpdatePubChatEvent(message));
        EventBus.getDefault().post(new BetsEndEvent(context,betsEndJson));
    }
}
