package com.whzl.mengbi.chat.room.message.events;

import android.content.Context;

import com.whzl.mengbi.chat.room.message.messageJson.AnchorWeekTaskJson;

/**
 * @author nobody
 * @date 2018/12/13
 */
public class AnchorWeekTaskEvent {
    public AnchorWeekTaskJson anchorWeekTaskJson;

    public AnchorWeekTaskEvent(Context context, AnchorWeekTaskJson anchorWeekTaskJson) {
        this.anchorWeekTaskJson = anchorWeekTaskJson;
    }
}
