package com.whzl.mengbi.chat.room.util;

import android.content.Context;
import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;


public abstract class NickNameSpan extends ClickableSpan {
    private Context context;

    //TODO:add color
    public NickNameSpan(Context mContext) {
        this.context = mContext;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        //TODO:修改color
        //ds.setColor(context.getResources().getColor(0));
        ds.setColor(Color.RED); //红色
        ds.setUnderlineText(false);
    }


}
