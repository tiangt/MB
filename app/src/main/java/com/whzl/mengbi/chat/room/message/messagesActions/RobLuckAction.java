package com.whzl.mengbi.chat.room.message.messagesActions;

import android.content.Context;

import com.whzl.mengbi.chat.room.message.events.RobLuckChangeEvent;
import com.whzl.mengbi.chat.room.message.events.RobNoPrizeEvent;
import com.whzl.mengbi.chat.room.message.events.RobPrizeEvent;
import com.whzl.mengbi.chat.room.message.events.RobRemindEvent;
import com.whzl.mengbi.chat.room.message.events.UpdatePubChatEvent;
import com.whzl.mengbi.chat.room.message.messageJson.RobLuckJson;
import com.whzl.mengbi.chat.room.message.messages.RobPrizeMessage;
import com.whzl.mengbi.chat.room.message.messages.RobRemindMessage;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * @author nobody
 * @date 2019/3/8
 */
public class RobLuckAction implements Actions {
    @Override
    public void performAction(String msgStr, Context context) {
        LogUtils.e("RobLuckAction  " + msgStr);
        RobLuckJson json = GsonUtils.GsonToBean(msgStr, RobLuckJson.class);
        if (null == json || json.context == null) {
            return;
        }
        //奖池数量
        if (json.context.busiCode.equals("prize_pool_change")) {
            EventBus.getDefault().post(new RobLuckChangeEvent(context, json));
        }
        if (json.context.busiCode.equals("ROB_NO_PRIZE")) {
            EventBus.getDefault().post(new RobNoPrizeEvent(context, json));
        }
        if (json.context.busiCode.equals("ROB_PRIZE")) {
            EventBus.getDefault().post(new UpdatePubChatEvent(new RobPrizeMessage(context, json)));
            EventBus.getDefault().post(new RobPrizeEvent(context, json));
        }
        if (json.context.busiCode.equals("OPEN_PRIZE_REMIND")) {
            EventBus.getDefault().post(new UpdatePubChatEvent(new RobRemindMessage(context, json)));
            EventBus.getDefault().post(new RobRemindEvent(context, json));
        }
    }
}
