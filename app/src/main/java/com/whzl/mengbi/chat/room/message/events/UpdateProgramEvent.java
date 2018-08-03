package com.whzl.mengbi.chat.room.message.events;

import android.content.Context;

import com.whzl.mengbi.chat.room.message.messageJson.UpdateProgramJson;

/**
 * author: yaobo wu
 * date:   On 2018/8/3
 */
public class UpdateProgramEvent {
    private UpdateProgramJson mProgramJson;
    private Context mContext;

    public UpdateProgramEvent(UpdateProgramJson mProgramJson, Context mContext) {
        this.mProgramJson = mProgramJson;
        this.mContext = mContext;
    }

    public UpdateProgramJson getmProgramJson() {
        return mProgramJson;
    }

    public Context getmContext() {
        return mContext;
    }
}
