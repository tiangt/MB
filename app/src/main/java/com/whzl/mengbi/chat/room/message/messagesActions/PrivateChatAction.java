package com.whzl.mengbi.chat.room.message.messagesActions;

import android.content.Context;

import com.whzl.mengbi.chat.room.message.events.UpdatePrivateChatEvent;
import com.whzl.mengbi.chat.room.message.events.UpdatePrivateChatUIEvent;
import com.whzl.mengbi.chat.room.message.messageJson.ChatCommonJson;
import com.whzl.mengbi.chat.room.message.messages.ChatMessage;
import com.whzl.mengbi.chat.room.util.ImageUrl;
import com.whzl.mengbi.gen.PrivateChatContentDao;
import com.whzl.mengbi.gen.PrivateChatUserDao;
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

        PrivateChatContentDao privateChatContentDao = BaseApplication.getInstance().getDaoSession().getPrivateChatContentDao();
        PrivateChatContent chatContent = new PrivateChatContent();
        chatContent.setContent(json.getContent());
        chatContent.setTimestamp(System.currentTimeMillis());
        chatContent.setFromId(Long.parseLong(json.getFrom_uid()));
        chatContent.setPrivateUserId(Long.parseLong(json.getFrom_uid()));
        chatContent.setUserId(Long.parseLong(SPUtils.get(BaseApplication.getInstance(), "userId", 0L).toString()));
        privateChatContentDao.insert(chatContent);

        if (Long.parseLong(json.getFrom_uid()) == Long.parseLong(SPUtils.get(BaseApplication.getInstance(), "userId", 0L).toString())) {
            return;
        }
        PrivateChatUserDao privateChatUserDao = BaseApplication.getInstance().getDaoSession().getPrivateChatUserDao();
        PrivateChatUser chatUser = privateChatUserDao.queryBuilder().where(PrivateChatUserDao.Properties.UserId.
                        eq(Long.parseLong(SPUtils.get(BaseApplication.getInstance(), "userId", 0L).toString())),
                PrivateChatUserDao.Properties.PrivateUserId.eq(json.getFrom_uid())).unique();
        if (chatUser == null) {
            chatUser = new PrivateChatUser();
            chatUser.setAvatar(ImageUrl.getAvatarUrl(Long.parseLong(json.getFrom_uid()), "jpg", System.currentTimeMillis()));
            chatUser.setName(json.getFrom_nickname());
            chatUser.setPrivateUserId(Long.valueOf(json.getFrom_uid()));
            chatUser.setUserId(Long.parseLong(SPUtils.get(BaseApplication.getInstance(), "userId", 0L).toString()));
            chatUser.setTimestamp(System.currentTimeMillis());
            privateChatUserDao.insert(chatUser);
        } else {
            long uncheckTime = chatUser.getUncheckTime();
            if (uncheckTime == 99) {
                chatUser.setUncheckTime((long) 99);
            } else {
                chatUser.setUncheckTime(++uncheckTime);
            }
            chatUser.setTimestamp(System.currentTimeMillis());
            chatUser.setId(chatUser.getId());
            privateChatUserDao.update(chatUser);
        }
        EventBus.getDefault().post(new UpdatePrivateChatUIEvent());
    }
}
