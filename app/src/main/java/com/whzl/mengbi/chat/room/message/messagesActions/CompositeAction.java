package com.whzl.mengbi.chat.room.message.messagesActions;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.whzl.mengbi.chat.room.message.events.CompositeEvent;
import com.whzl.mengbi.chat.room.message.events.UpdatePubChatEvent;
import com.whzl.mengbi.chat.room.message.messageJson.CompositeJson;
import com.whzl.mengbi.chat.room.message.messages.CompositeMessage;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

/**
 * @author cliang
 * @date 2019.1.15
 */
public class CompositeAction implements Actions {

    @Override
    public void performAction(String msgStr, Context context) {
        Log.i("clTest", "CompositeAction -> " + msgStr);
        CompositeJson compositeJson = GsonUtils.GsonToBean(msgStr, CompositeJson.class);
        if (null == compositeJson || compositeJson.context == null) {
            return;
        }
        CompositeMessage compositeMessage = new CompositeMessage(context, compositeJson);
        UpdatePubChatEvent pubChatEvent = new UpdatePubChatEvent(compositeMessage);
        EventBus.getDefault().post(pubChatEvent);
    }
}
