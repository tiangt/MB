package com.whzl.mengbi.chat.room.message.events;

import android.content.Context;

import com.whzl.mengbi.chat.room.message.messageJson.WeekStarJson;

/**
 * @author nobody
 * @date 2018/12/6
 */
public class WeekStarEvent {
    public Context context;
    public WeekStarJson weekStarJson;

    public WeekStarEvent(Context context, WeekStarJson weekStarJson) {
        this.context = context;
        this.weekStarJson = weekStarJson;
    }
}
