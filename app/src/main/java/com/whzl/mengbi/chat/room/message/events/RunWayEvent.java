package com.whzl.mengbi.chat.room.message.events;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.view.View;
import android.widget.TextView;

import com.whzl.mengbi.chat.room.message.messageJson.RunWayJson;
import com.whzl.mengbi.chat.room.util.LightSpanString;
import com.whzl.mengbi.util.DateUtils;

public class RunWayEvent {
    private Context mContext;
    private RunWayJson runWayJson;
    private SpannableString giftSpanString;
    private boolean hasRuned;

    /*
    giftSpanString 可能为null,使用时请判断是否为null
    文字的样式显示可以调用LightSpanString.getLightString()方法
     */
    public RunWayEvent(Context mContext, RunWayJson runWayJson, SpannableString giftSpanString) {
        this.mContext = mContext;
        this.runWayJson = runWayJson;
        this.giftSpanString = giftSpanString;

    }

    public RunWayEvent(RunWayJson runWayJson, SpannableString giftSpanString) {
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

    public void showRunWay(TextView tvRunWayGift) throws Exception {
        String type = runWayJson.getContext().getRunwayType();
        tvRunWayGift.setText(LightSpanString.getLightString(DateUtils.longToDate(runWayJson.getContext().getDateLong()) + " ",
                Color.parseColor("#faf9f9"))); //时间
        tvRunWayGift.append(LightSpanString.getLightString(runWayJson.getContext().getNickname(),
                Color.parseColor("#fff00a"))); //用户
        tvRunWayGift.append(LightSpanString.getLightString(" 送 ", Color.parseColor("#faf9f9")));
        tvRunWayGift.append(LightSpanString.getLightString(runWayJson.getContext().getToNickname(),
                Color.parseColor("#fff00a"))); //昵称
        tvRunWayGift.append(LightSpanString.getLightString(" ",
                Color.parseColor("#faf9f9")));
        tvRunWayGift.append(LightSpanString.getLightString(runWayJson.getContext().getGoodsName(),
                Color.parseColor("#faf9f9"))); //昵称
        tvRunWayGift.append(LightSpanString.getLightString(" + ",
                Color.parseColor("#faf9f9")));
        if (getGiftSpanString() != null) {
            tvRunWayGift.append(getGiftSpanString());//礼物
        }
        tvRunWayGift.append(LightSpanString.getLightString(" x" + runWayJson.getContext().getCount(),
                Color.parseColor("#faf9f9"))); //个数
        if ("destroy".equals(type)) {
            tvRunWayGift.append(LightSpanString.getLightString(" 攻占超跑，",
                    Color.parseColor("#faf9f9")));
        } else if ("getOn".equals(type)) {
            tvRunWayGift.append(LightSpanString.getLightString(" 登上超跑，",
                    Color.parseColor("#faf9f9")));
        }
        tvRunWayGift.append(LightSpanString.getLightString(" " + "围观ing",
                Color.parseColor("#fff00a")));
    }

    /*
    获取跑道礼物的总值
     */
    public int getTotalPrice() {
        return runWayJson.getContext().getPrice() * runWayJson.getContext().getCount();
    }

    public boolean isHasRuned() {
        return hasRuned;
    }

    public void setHasRuned(boolean hasRuned) {
        this.hasRuned = hasRuned;
    }
}
