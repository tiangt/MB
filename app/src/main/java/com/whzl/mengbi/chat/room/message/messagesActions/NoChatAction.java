package com.whzl.mengbi.chat.room.message.messagesActions;

import android.content.Context;

import com.whzl.mengbi.chat.room.message.events.KickoutEvent;
import com.whzl.mengbi.chat.room.message.events.UpdatePubChatEvent;
import com.whzl.mengbi.chat.room.message.messages.NoChatMsg;
import com.whzl.mengbi.util.SPUtils;

import org.greenrobot.eventbus.EventBus;

public class NoChatAction  {

    public void performAction(NoChatMsg noChatMsg, Context context) {
        if (noChatMsg.getNochatType() == KickoutEvent.KICKOUT_CODE ||
                noChatMsg.getNochatType() == KickoutEvent.LOGOUT_CODE) {
            int loginUid = Integer.parseInt(SPUtils.get(context, "userId", 0).toString());
            if (0 == loginUid) {
                String nickname = SPUtils.get(context, "nickname", "").toString();
                if (nickname.equals(noChatMsg.getToNickname())) {
                    EventBus.getDefault().post(new KickoutEvent(noChatMsg));
                }
            }
            EventBus.getDefault().post(new KickoutEvent(noChatMsg));
        }else {
            EventBus.getDefault().post(new UpdatePubChatEvent(noChatMsg));
        }
    }
}
