package com.whzl.mengbi.chat.room.util;

import android.content.Context;
import android.graphics.Color;
import android.text.TextPaint;
import android.text.style.ClickableSpan;

import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.whzl.mengbi.R;
import com.whzl.mengbi.util.UIUtil;


public abstract class NickNameSpan extends ClickableSpan {
    private Context context;
    private int color = -1;

    //TODO:add color
    public NickNameSpan(Context mContext) {
        this.context = mContext;
    }

    public NickNameSpan(Context context, int color) {
        this.context = context;
        this.color = color;
    }
    @Override
    public void updateDrawState(TextPaint ds) {
        //TODO:修改color
        //ds.setColor(context.getResources().getColor(0));
        int dsColor = Color.parseColor("#75bbfb");
        if (this.color != -1) {
            dsColor = this.color;
        }
        ds.setColor(dsColor); //红色
        ds.setUnderlineText(false);
//        float textSize = DensityUtil.dp2px(15);
        float textSize = UIUtil.sp2px(context, 13);
        //Log.d("NicknameSpan", textSize + "");
        ds.setTextSize(textSize);
    }
}




