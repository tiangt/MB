package com.whzl.mengbi.chat.room.util;

import android.content.Context;
import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;

import com.whzl.mengbi.util.UIUtil;


public abstract class ClickSpan extends ClickableSpan {
    private Context context;
    private int color = -1;

    public ClickSpan(Context mContext) {
        this.context = mContext;
    }

    public ClickSpan(Context context, int color) {
        this.context = context;
        this.color = color;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(color); //红色
        ds.setUnderlineText(true);
        float textSize = UIUtil.sp2px(context, 14);
        ds.setTextSize(textSize);
    }
}




