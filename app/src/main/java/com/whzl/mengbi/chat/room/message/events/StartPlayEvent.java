package com.whzl.mengbi.chat.room.message.events;

import android.content.Context;

import com.whzl.mengbi.chat.room.message.messageJson.StartStopLiveJson;

public class StartPlayEvent {
    private StartStopLiveJson startStopLiveJson;
    private Context mContext;

    public StartPlayEvent(StartStopLiveJson startStopLiveJson, Context mContext) {
        this.startStopLiveJson = startStopLiveJson;
        this.mContext = mContext;
    }

    public StartStopLiveJson getStartStopLiveJson() {
        return startStopLiveJson;
    }

    public Context getmContext() {
        return mContext;
    }

    public String getStreamAddress() {
        String streamAddress = startStopLiveJson.getContext().getShow_streams().get(0).getStreamAddress();
        for(StartStopLiveJson.ShowStreams showStreams : startStopLiveJson.getContext().getShow_streams()) {
            if (showStreams.getStreamType().equals("rtmp")) {
                streamAddress = showStreams.getStreamAddress();
                break;
            }
        }
        return streamAddress;
    }

}
