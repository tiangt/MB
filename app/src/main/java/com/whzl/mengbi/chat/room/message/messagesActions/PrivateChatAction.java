package com.whzl.mengbi.chat.room.message.messagesActions;

import android.content.Context;

import com.whzl.mengbi.chat.room.message.events.UpdatePrivateChatEvent;
import com.whzl.mengbi.chat.room.message.events.UpdatePubChatEvent;
import com.whzl.mengbi.chat.room.message.messageJson.ChatCommonJson;
import com.whzl.mengbi.chat.room.message.messages.ChatMessage;
import com.whzl.mengbi.gen.PrivateChatContentDao;
import com.whzl.mengbi.greendao.PrivateChatContent;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.util.GsonUtils;

import org.greenrobot.eventbus.EventBus;


public class PrivateChatAction implements Actions{

    @Override
    public void performAction(String msgStr, Context context) {
        ChatCommonJson json = GsonUtils.GsonToBean(msgStr,ChatCommonJson.class);
        if(json == null){
            return;
        }
        ChatMessage message = new ChatMessage(json,context, null, true);
        EventBus.getDefault().post(new UpdatePrivateChatEvent(message));

        PrivateChatContentDao privateChatContentDao = BaseApplication.getInstance().getDaoSession().getPrivateChatContentDao();
        PrivateChatContent chatContent = new PrivateChatContent();
        chatContent.setContent(json.getContent());
        chatContent.setTimestamp(System.currentTimeMillis());
        chatContent.setFromId(Long.parseLong(json.getFrom_uid()));
        chatContent.setPrivateUserId(Long.parseLong(json.getFrom_uid()));
        privateChatContentDao.insert(chatContent);
    }
}
