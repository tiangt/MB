package com.whzl.mengbi.chat.room.message.messagesActions;

import android.content.Context;

import com.whzl.mengbi.chat.room.message.events.UpdatePubChatEvent;
import com.whzl.mengbi.chat.room.message.messageJson.OpenGuardJson;
import com.whzl.mengbi.chat.room.message.messages.OpenGuardMessage;
import com.whzl.mengbi.chat.room.util.ImageUrl;
import com.whzl.mengbi.chat.room.message.events.GuardOpenEvent;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

public class OpenGuardAction implements Actions {
    @Override
    public void performAction(String msgStr, Context context) {
        LogUtils.e("sssssss   "+msgStr);
        OpenGuardJson openGuardJson = GsonUtils.GsonToBean(msgStr, OpenGuardJson.class);
        if (null == openGuardJson) {
            return;
        }
        OpenGuardMessage openGuardMessage = new OpenGuardMessage(openGuardJson, context);
        UpdatePubChatEvent updatePubChatEvent = new UpdatePubChatEvent(openGuardMessage);
        EventBus.getDefault().post(updatePubChatEvent);
        String avatar = ImageUrl.getAvatarUrl(openGuardJson.getContext().getUserId(), "", System.currentTimeMillis());
        EventBus.getDefault().post(new GuardOpenEvent(avatar, openGuardJson.getContext().getNickname(), openGuardJson.getContext().getUserId()));
    }
}
