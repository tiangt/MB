package com.whzl.mengbi.chat.room.message.events;

import android.content.Context;

import com.whzl.mengbi.chat.room.message.messageJson.PkJson;

public class PkEvent {
    private PkJson pkJson;
    private Context mContext;
    private int programId;

    public void setProgramId(int programId) {
        this.programId = programId;
    }

    public PkEvent(PkJson pkJson, Context mContext) {
        super();
        this.pkJson = pkJson;
        this.mContext = mContext;
    }

    public PkJson getPkJson() {
        return pkJson;
    }

    public Context getmContext() {
        return mContext;
    }
}
