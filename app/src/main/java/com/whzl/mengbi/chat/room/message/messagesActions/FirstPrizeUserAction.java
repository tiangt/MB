package com.whzl.mengbi.chat.room.message.messagesActions;

import android.content.Context;

import com.whzl.mengbi.chat.room.message.events.UpdatePubChatEvent;
import com.whzl.mengbi.chat.room.message.messageJson.BetsEndJson;
import com.whzl.mengbi.chat.room.message.messageJson.FirstPrizeUserJson;
import com.whzl.mengbi.chat.room.message.messages.BetsEndMessage;
import com.whzl.mengbi.chat.room.message.messages.FirsrPrizeUserMessage;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * @author nobody
 * @date 2019/1/8
 */
public class FirstPrizeUserAction implements Actions {
    @Override
    public void performAction(String msgStr, Context context) {
        LogUtils.e("FirstPrizeUserAction  " + msgStr);
        FirstPrizeUserJson betsEndJson = GsonUtils.GsonToBean(msgStr, FirstPrizeUserJson.class);
        if (null == betsEndJson || betsEndJson.context == null) {
            return;
        }
        FirsrPrizeUserMessage message = new FirsrPrizeUserMessage(betsEndJson, context);
        EventBus.getDefault().post(new UpdatePubChatEvent(message));
    }
}
