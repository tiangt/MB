package com.whzl.mengbi.chat.room.message.events;

import android.content.Context;

import com.whzl.mengbi.chat.room.message.messageJson.RobLuckJson;

/**
 * @author nobody
 * @date 2019/3/8
 */
public class RobLuckChangeEvent {
    public Context context;
    public RobLuckJson robLuckJson;

    public RobLuckChangeEvent(Context context, RobLuckJson json) {
        this.context = context;
        robLuckJson = json;
    }
}
