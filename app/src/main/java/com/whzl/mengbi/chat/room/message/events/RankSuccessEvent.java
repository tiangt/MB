package com.whzl.mengbi.chat.room.message.events;

import android.content.Context;

import com.whzl.mengbi.chat.room.message.messageJson.RankSuccessJson;

/**
 * @author nobody
 * @date 2019-08-01
 */
public class RankSuccessEvent {
    public final Context context;
    public final RankSuccessJson rankSuccessJson;

    public RankSuccessEvent(Context context, RankSuccessJson rankSuccessJson) {
        this.context = context;
        this.rankSuccessJson = rankSuccessJson;
    }
}
