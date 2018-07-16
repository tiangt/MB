package com.whzl.mengbi.chat.room.message.events;

import android.content.Context;
import android.text.SpannableString;

import com.whzl.mengbi.chat.room.message.messageJson.RunWayJson;

public class RunWayEvent {
    private Context mContext;
    private RunWayJson runWayJson;
    private SpannableString giftSpanString;

    /*
    giftSpanString 可能为null,使用时请判断是否为null
    文字的样式显示可以调用LightSpanString.getLightString()方法
     */
    public RunWayEvent(Context mContext, RunWayJson runWayJson, SpannableString giftSpanString) {
        this.mContext = mContext;
        this.runWayJson = runWayJson;
        this.giftSpanString = giftSpanString;

    }

    public Context getmContext() {
        return mContext;
    }

    public RunWayJson getRunWayJson() {
        return runWayJson;
    }

    public SpannableString getGiftSpanString() {
        return giftSpanString;
    }

    /*
    获取跑道礼物的总值
     */
    public int getTotalPrice() {
        return runWayJson.getContext().getPrice() * runWayJson.getContext().getCount();
    }
}
