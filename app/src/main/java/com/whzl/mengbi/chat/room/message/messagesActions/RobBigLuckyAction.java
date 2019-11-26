package com.whzl.mengbi.chat.room.message.messagesActions;

import android.content.Context;

import com.whzl.mengbi.chat.room.message.events.RobBigPrizeEvent;
import com.whzl.mengbi.chat.room.message.events.RobBigPrizePoolChangeEvent;
import com.whzl.mengbi.chat.room.message.events.RobBigRemindEvent;
import com.whzl.mengbi.chat.room.message.events.UpdatePubChatEvent;
import com.whzl.mengbi.chat.room.message.messageJson.RobBigLuckyJson;
import com.whzl.mengbi.chat.room.message.messages.RobBigPrizeRemindMessage;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * @author nobody
 * @date 2019-11-22
 */
public class RobBigLuckyAction implements Actions {
    @Override
    public void performAction(String msgStr, Context context) {
        LogUtils.e("RobBigLuckyAction  " + msgStr);
        RobBigLuckyJson robBigLuckyJson = GsonUtils.GsonToBean(msgStr, RobBigLuckyJson.class);
        if (robBigLuckyJson != null && robBigLuckyJson.context != null) {
            if ("big_prize_pool_change".equals(robBigLuckyJson.context.busiCode)) {
                EventBus.getDefault().post(new RobBigPrizePoolChangeEvent(context, robBigLuckyJson));
            } else if ("OPEN_BIG_PRIZE_REMIND".equals(robBigLuckyJson.context.busiCode)) {
                EventBus.getDefault().post(new UpdatePubChatEvent(new RobBigPrizeRemindMessage(context, robBigLuckyJson)));
                EventBus.getDefault().post(new RobBigRemindEvent(context, robBigLuckyJson));
            } else if ("ROB_BIG_PRIZE".equals(robBigLuckyJson.context.busiCode)) {
                EventBus.getDefault().post(new RobBigPrizeEvent(context, robBigLuckyJson));
            }
        }
    }
}
