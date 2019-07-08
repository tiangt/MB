package com.whzl.mengbi.chat.room.message.messagesActions;

import android.content.Context;

import com.whzl.mengbi.chat.room.message.events.UpdatePrivateChatEvent;
import com.whzl.mengbi.chat.room.message.events.UpdatePrivateChatUIEvent;
import com.whzl.mengbi.chat.room.message.messageJson.ChatCommonJson;
import com.whzl.mengbi.chat.room.message.messageJson.FromJson;
import com.whzl.mengbi.chat.room.message.messages.ChatMessage;
import com.whzl.mengbi.chat.room.util.ImageUrl;
import com.whzl.mengbi.greendao.ChatDbUtils;
import com.whzl.mengbi.greendao.PrivateChatContent;
import com.whzl.mengbi.greendao.PrivateChatUser;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.SPUtils;

import org.greenrobot.eventbus.EventBus;


public class PrivateChatAction implements Actions {

    @Override
    public void performAction(String msgStr, Context context) {
        ChatCommonJson json = GsonUtils.GsonToBean(msgStr, ChatCommonJson.class);
        if (json == null) {
            return;
        }
        ChatMessage message = new ChatMessage(json, context, null, true);
        EventBus.getDefault().post(new UpdatePrivateChatEvent(message));

        PrivateChatContent chatContent = new PrivateChatContent();
        chatContent.setContent(json.getContent());
        chatContent.setTimestamp(System.currentTimeMillis());
        chatContent.setFromId(Long.parseLong(json.getFrom_uid()));
        chatContent.setPrivateUserId(Long.parseLong(json.getFrom_uid()));
        chatContent.setUserId(Long.parseLong(SPUtils.get(BaseApplication.getInstance(), "userId", 0L).toString()));
        ChatDbUtils.getInstance().insertChatContent(chatContent);

        if (Long.parseLong(json.getFrom_uid()) == Long.parseLong(SPUtils.get(BaseApplication.getInstance(), "userId", 0L).toString())) {
            return;
        }
        PrivateChatUser chatUser = new PrivateChatUser();
        chatUser.setAvatar(ImageUrl.getAvatarUrl(Long.parseLong(json.getFrom_uid()), "jpg", System.currentTimeMillis()));
        chatUser.setName(json.getFrom_nickname());
        chatUser.setPrivateUserId(Long.valueOf(json.getFrom_uid()));
        chatUser.setUserId(Long.parseLong(SPUtils.get(BaseApplication.getInstance(), "userId", 0L).toString()));
        chatUser.setTimestamp(System.currentTimeMillis());
        chatUser.setLastMessage(json.getContent());
        for (int i = 0; i < json.getFrom_json().getLevelList().size(); i++) {
            FromJson.Level level = json.getFrom_json().getLevelList().get(i);
            if (level.equals("USER_LEVEL")) {
                chatUser.setIsAnchor(false);
                chatUser.setUserLevel(level.getLevelValue());
            }
            if ("ANCHOR_LEVEL".equals(level)) {
                chatUser.setIsAnchor(true);
                chatUser.setAnchorLevel(level.getLevelValue());
            }
        }

        ChatDbUtils.getInstance().updatePrivateChatUser(
                Long.parseLong(SPUtils.get(BaseApplication.getInstance(), "userId", 0L).toString()), chatUser);
        EventBus.getDefault().post(new UpdatePrivateChatUIEvent());
    }
}
