package com.whzl.mengbi.chat.room.message.messagesActions;

import android.content.Context;

import com.whzl.mengbi.chat.room.message.events.GuessEvent;
import com.whzl.mengbi.chat.room.message.events.UpdatePubChatEvent;
import com.whzl.mengbi.chat.room.message.messageJson.GuessJson;
import com.whzl.mengbi.chat.room.message.messages.GuessMessage;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * @author nobody
 * @date 2019-06-13
 */
public class GuessAction implements Actions {
    @Override
    public void performAction(String msgStr, Context context) {
        LogUtils.e("GuessAction   " + msgStr);
        GuessJson guessJson = GsonUtils.GsonToBean(msgStr, GuessJson.class);
        if (guessJson == null || guessJson.context == null) {
            return;
        }
        EventBus.getDefault().post(new GuessEvent(context, guessJson));
        if ("USER_GUESS".equals(guessJson.context.busicode)) {
            EventBus.getDefault().post(new UpdatePubChatEvent(new GuessMessage(context, guessJson)));
        }
    }
}
