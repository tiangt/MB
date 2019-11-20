package com.whzl.mengbi.ui.widget.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.whzl.mengbi.util.UIUtil;

/**
 * @author nobody
 * @date 2019-11-20
 */
public class SnatchTextView extends View {
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private String text = "输入夺宝次数进行夺宝，满足开奖条件后会从本期夺宝用户中选中一位用户独得本轮奖励。";
    float measuredWidth[] = new float[2];

    public SnatchTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    {
        paint.setTextSize(UIUtil.sp2px(getContext(), 9.9f));
        paint.setColor(Color.parseColor("#a19be6"));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        int index = paint.breakText(text, false, getWidth(), measuredWidth);
        canvas.drawText(text, 0, index, 0, -fontMetrics.ascent, paint);
        canvas.drawText(text, index, text.length(), 0, -fontMetrics.ascent + paint.getFontSpacing(), paint);
    }
}
