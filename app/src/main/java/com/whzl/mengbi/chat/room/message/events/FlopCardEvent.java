package com.whzl.mengbi.chat.room.message.events;

import android.content.Context;

import com.whzl.mengbi.chat.room.message.messageJson.FlopCardJson;

/**
 * @author nobody
 * @date 2019-06-21
 */
public class FlopCardEvent {
    public  FlopCardJson flopCardJson;
    public  Context context;

    public FlopCardEvent(FlopCardJson flopCardJson, Context context) {
        this.flopCardJson = flopCardJson;
        this.context = context;
    }
}
