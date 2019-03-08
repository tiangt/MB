package com.whzl.mengbi.chat.room.util;

import android.content.Context;
import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;

import com.whzl.mengbi.util.UIUtil;


public abstract class RobSpan extends ClickableSpan {
    private Context context;
    private int color = -1;

    public RobSpan(Context mContext) {
        this.context = mContext;
    }

    public RobSpan(Context context, int color) {
        this.context = context;
        this.color = color;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        //ds.setColor(context.getResources().getColor(0));
        int dsColor = Color.parseColor("#75bbfb");
        if (this.color != -1) {
            dsColor = this.color;
        }
        ds.setColor(dsColor); //红色
        ds.setUnderlineText(true);
        float textSize = UIUtil.sp2px(context, 14);
        ds.setTextSize(textSize);
    }
}




