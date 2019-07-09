package com.whzl.mengbi.chat.room.util;

import android.content.Context;
import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;

import com.whzl.mengbi.util.UIUtil;


public abstract class ClickSpan extends ClickableSpan {
    private boolean underLine = false;
    private float textSize = 14;
    private Context context;
    private int color = -1;

    public ClickSpan(Context mContext, int color, boolean underLine, int textSize) {
        this.context = mContext;
        this.color = color;
        this.underLine = underLine;
        this.textSize = textSize;
    }


    @Override
    public void updateDrawState(TextPaint ds) {
        ds.setColor(color); //红色
        ds.setUnderlineText(underLine);
        ds.setTextSize(UIUtil.sp2px(context, textSize));
    }
}




