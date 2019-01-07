package com.whzl.mengbi.chat.room.message.messagesActions;

import android.content.Context;

import com.whzl.mengbi.chat.room.message.events.UpdatePubChatEvent;
import com.whzl.mengbi.chat.room.message.messageJson.PrizePoolFullJson;
import com.whzl.mengbi.chat.room.message.messages.PrizePoolFullMessage;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * @author nobody
 * @date 2019/1/7
 */
public class PrizePoolFullAction implements Actions {
    @Override
    public void performAction(String msgStr, Context context) {
        LogUtils.e("PrizePoolFullAction  " + msgStr);
        PrizePoolFullJson prizePoolFullJson = GsonUtils.GsonToBean(msgStr, PrizePoolFullJson.class);
        if (null == prizePoolFullJson || prizePoolFullJson.context == null) {
            return;
        }
        PrizePoolFullMessage message = new PrizePoolFullMessage(prizePoolFullJson, context);
        EventBus.getDefault().post(new UpdatePubChatEvent(message));
    }
}
