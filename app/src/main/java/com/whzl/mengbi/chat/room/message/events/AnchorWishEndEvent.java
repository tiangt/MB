package com.whzl.mengbi.chat.room.message.events;

import android.content.Context;

import com.whzl.mengbi.chat.room.message.messageJson.AnchorWishJson;

/**
 * @author nobody
 * @date 2019/3/28
 */
public class AnchorWishEndEvent {
    private Context context;
    public AnchorWishJson anchorWishJson;

    public AnchorWishEndEvent(Context context, AnchorWishJson anchorWishJson) {
        this.context = context;
        this.anchorWishJson = anchorWishJson;
    }

}
