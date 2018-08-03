package com.whzl.mengbi.chat.room.message.events;

import android.content.Context;

import com.whzl.mengbi.chat.room.message.messageJson.ProgramFirstNotifyJson;

/**
 * author: yaobo wu
 * date:   On 2018/8/3
 * 场榜第一通知
 */
public class ProgramFirstNotifyEvent {
    private ProgramFirstNotifyJson mProgramFirst;
    private Context context;

    public ProgramFirstNotifyEvent(ProgramFirstNotifyJson mProgramFirst, Context context) {
        this.mProgramFirst = mProgramFirst;
        this.context = context;
    }

    public ProgramFirstNotifyJson getmProgramFirst() {
        return mProgramFirst;
    }

    public Context getContext() {
        return context;
    }
}
