package com.whzl.mengbi.chat.room.message.events;

import android.content.Context;

import com.whzl.mengbi.chat.room.message.messageJson.CompositeJson;

/**
 * @author cliang
 * @date 2019.1.15
 */
public class CompositeEvent {

    private Context context;
    private CompositeJson compositeJson;

    public CompositeEvent(Context context, CompositeJson compositeJson){
        this.context = context;
        this.compositeJson = compositeJson;
    }
}
