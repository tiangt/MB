package com.whzl.mengbi.chat.room.message.messagesActions;

import android.content.Context;

import com.whzl.mengbi.chat.room.message.events.FirstPrizeUserEvent;
import com.whzl.mengbi.chat.room.message.events.PrizePoolFullEvent;
import com.whzl.mengbi.chat.room.message.events.UpdatePubChatEvent;
import com.whzl.mengbi.chat.room.message.messageJson.DoubleBallJson;
import com.whzl.mengbi.chat.room.message.messageJson.FirstPrizeUserJson;
import com.whzl.mengbi.chat.room.message.messageJson.PrizePoolFullJson;
import com.whzl.mengbi.chat.room.message.messages.FirsrPrizeUserMessage;
import com.whzl.mengbi.chat.room.message.messages.PrizePoolFullMessage;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * @author nobody
 * @date 2019/3/15
 */
public class DoubleColorBallAction implements Actions {
    @Override
    public void performAction(String msgStr, Context context) {

        LogUtils.e("DoubleColorBallAction  " + msgStr);
        DoubleBallJson doubleBallJson = GsonUtils.GsonToBean(msgStr, DoubleBallJson.class);
        if (null == doubleBallJson || doubleBallJson.context == null) {
            return;
        }

        if (doubleBallJson.context.busiCode.equals("PRIZE_POOL_FULL")) {
            PrizePoolFullJson prizePoolFullJson = GsonUtils.GsonToBean(msgStr, PrizePoolFullJson.class);
            if (null == prizePoolFullJson || prizePoolFullJson.context == null) {
                return;
            }
            PrizePoolFullMessage message = new PrizePoolFullMessage(prizePoolFullJson, context);
            EventBus.getDefault().post(new UpdatePubChatEvent(message));
            EventBus.getDefault().post(new PrizePoolFullEvent(context, prizePoolFullJson));
        }

        if (doubleBallJson.context.busiCode.equals("FIRST_PRIZE")) {
            FirstPrizeUserJson betsEndJson = GsonUtils.GsonToBean(msgStr, FirstPrizeUserJson.class);
            if (null == betsEndJson || betsEndJson.context == null) {
                return;
            }
            FirsrPrizeUserMessage message2 = new FirsrPrizeUserMessage(betsEndJson, context);
            EventBus.getDefault().post(new UpdatePubChatEvent(message2));
            EventBus.getDefault().post(new FirstPrizeUserEvent(context, betsEndJson));
        }

    }
}
