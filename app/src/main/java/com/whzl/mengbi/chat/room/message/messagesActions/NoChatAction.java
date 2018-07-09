package com.whzl.mengbi.chat.room.message.messagesActions;

import android.content.Context;

import com.whzl.mengbi.chat.room.message.events.KickoutEvent;
import com.whzl.mengbi.chat.room.message.events.UpdatePubChatEvent;
import com.whzl.mengbi.chat.room.message.messages.NoChatMsg;

import org.greenrobot.eventbus.EventBus;

public class NoChatAction  {

    final int KICKOUT_CODE = 8; //被提出房间
    final int LOGOUT_CODE = 12; //用多个手机打开同一个直播间，强制退出之前的直播间
    public void performAction(NoChatMsg noChatMsg, Context context) {
        if (noChatMsg.getNochatType() == KICKOUT_CODE ||
                noChatMsg.getNochatType() == LOGOUT_CODE) {
            EventBus.getDefault().post(new KickoutEvent(noChatMsg));
        }else {
            EventBus.getDefault().post(new UpdatePubChatEvent(noChatMsg));
        }
    }
}
