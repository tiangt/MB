package com.whzl.mengbi.chat.room.message.messagesActions;

import android.content.Context;

import com.whzl.mengbi.chat.room.message.events.AnchorWishBeginEvent;
import com.whzl.mengbi.chat.room.message.events.AnchorWishChangeEvent;
import com.whzl.mengbi.chat.room.message.events.AnchorWishEndEvent;
import com.whzl.mengbi.chat.room.message.events.UpdatePubChatEvent;
import com.whzl.mengbi.chat.room.message.messageJson.AnchorWishJson;
import com.whzl.mengbi.chat.room.message.messages.AnchorWishAwardMessage;
import com.whzl.mengbi.chat.room.message.messages.AnchorWishSuccessMessage;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * @author nobody
 * @date 2019/3/28
 */
public class AnchorWishAction implements Actions {
    @Override
    public void performAction(String msgStr, Context context) {
        LogUtils.e("AnchorWishAction  " + msgStr);
        AnchorWishJson anchorWishJson = GsonUtils.GsonToBean(msgStr, AnchorWishJson.class);
        if (anchorWishJson == null || anchorWishJson.context == null) {
            return;
        }

        if (anchorWishJson.context.busicode.equals("USER_WISH")) {
            EventBus.getDefault().post(new AnchorWishBeginEvent(context, anchorWishJson));
        }
        if (anchorWishJson.context.busicode.equals("RANK_CHANGE")) {
            EventBus.getDefault().post(new AnchorWishChangeEvent(context, anchorWishJson));
        }
        if (anchorWishJson.context.busicode.equals("WISH_SUCCESS")) {
            EventBus.getDefault().post(new AnchorWishEndEvent(context, anchorWishJson));
        }
        if (anchorWishJson.context.busicode.equals("WISH_AWARD")) {
            if (anchorWishJson.context.gameWishAwardList == null || anchorWishJson.context.gameWishAwardList.size() == 0) {
                return;
            }
            for (int i = 0; i < anchorWishJson.context.gameWishAwardList.size(); i++) {
                AnchorWishAwardMessage message = new AnchorWishAwardMessage(context, anchorWishJson.context.gameWishAwardList.get(i));
                EventBus.getDefault().post(new UpdatePubChatEvent(message));
            }
        }

        if (anchorWishJson.context.busicode.equals("WISH_SUCCESS")) {
            AnchorWishSuccessMessage message = new AnchorWishSuccessMessage(context, anchorWishJson);
            EventBus.getDefault().post(new UpdatePubChatEvent(message));
        }
    }
}
