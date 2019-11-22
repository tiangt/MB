package com.whzl.mengbi.chat.room.message.events;

import android.content.Context;

import com.whzl.mengbi.chat.room.message.messageJson.RobBigLuckyJson;

/**
 * @author nobody
 * @date 2019-11-22
 */
public class RobBigLuckyEvent {
    public final Context context;
    public final RobBigLuckyJson robBigLuckyJson;

    public RobBigLuckyEvent(Context context, RobBigLuckyJson robBigLuckyJson) {
        this.context = context;
        this.robBigLuckyJson = robBigLuckyJson;
    }
}
