package com.whzl.mengbi.chat.room.message.events;

import android.content.Context;

import com.whzl.mengbi.chat.room.message.messageJson.PlayNotifyJson;

/**
 * @author nobody
 * @date 2019/1/14
 */
public class PlayNotifyEvent {
    public Context context;
    public PlayNotifyJson playNotifyJson;

    public PlayNotifyEvent(Context context, PlayNotifyJson playNotifyJson) {
        this.context = context;
        this.playNotifyJson = playNotifyJson;
    }
}
