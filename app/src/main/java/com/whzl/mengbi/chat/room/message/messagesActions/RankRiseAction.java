package com.whzl.mengbi.chat.room.message.messagesActions;

import android.content.Context;

import com.whzl.mengbi.chat.room.message.events.UpdatePubChatEvent;
import com.whzl.mengbi.chat.room.message.messageJson.RankRiseJson;
import com.whzl.mengbi.chat.room.message.messages.RankRiseMessage;
import com.whzl.mengbi.util.GsonUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * @author nobody
 * @date 2019/3/1
 */
public class RankRiseAction implements Actions {
    @Override
    public void performAction(String msgStr, Context context) {
        RankRiseJson json = GsonUtils.GsonToBean(msgStr, RankRiseJson.class);
        if (null == json || json.context == null) {
            return;
        }
        EventBus.getDefault().post(new UpdatePubChatEvent(new RankRiseMessage(json, context)));
    }
}
