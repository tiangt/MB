package com.whzl.mengbi.chat.room.message.events;

import android.content.Context;

import com.whzl.mengbi.chat.room.message.messageJson.RobLuckJson;

/**
 * @author nobody
 * @date 2019/3/8
 */
public class RobPrizeEvent {
    private Context context;
    public RobLuckJson robLuckJson;

    public RobPrizeEvent(Context context, RobLuckJson json) {
        this.robLuckJson = json;
    }
}
