package com.whzl.mengbi.chat.room.message.messagesActions;

import android.content.Context;

import com.whzl.mengbi.chat.room.message.events.BroadCastBottomEvent;
import com.whzl.mengbi.chat.room.message.messageJson.BroadCastBottomJson;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;

import org.greenrobot.eventbus.EventBus;


/**
 * @author nobody
 */
public class BroadCastAction implements Actions {
    @Override
    public void performAction(String msgStr, Context context) {
        LogUtils.e("BroadCastAction  " + msgStr);
        BroadCastBottomJson broadCastJson = GsonUtils.GsonToBean(msgStr, BroadCastBottomJson.class);
        if (null == broadCastJson || broadCastJson.context == null) {
            return;
        }
        EventBus.getDefault().post(new BroadCastBottomEvent(broadCastJson, context));
    }


}
