package com.whzl.mengbi.chat.room.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.text.style.ReplacementSpan;

import com.whzl.mengbi.util.UIUtil;

/**
 * @author nobody
 * @date 2019/4/12
 */
public class RoundSpan extends ReplacementSpan {
    private int bgColor;
    private int endColor;
    private int textColor;
    private Context context;
    private final Paint tvPaint;

    public RoundSpan(Context context, int bgColor, int endcolor, int textColor) {
        super();
        this.bgColor = bgColor;
        this.endColor = endcolor;
        this.textColor = textColor;
        this.context = context;
        tvPaint = new Paint();
    }

    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        paint.setTextSize(UIUtil.sp2px(context, 10));
        return ((int) paint.measureText(text, start, end) + 24);
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {

        LinearGradient lg = new LinearGradient(x, top, x + ((int) paint.measureText(text, start, end)) + 24, x + paint.measureText(text, start, end),
                bgColor, endColor, Shader.TileMode.CLAMP);
        paint.setShader(lg);
        canvas.drawRoundRect(new RectF(x, top + 5, x + ((int) paint.measureText(text, start, end)) + 24, bottom - 5), 30, 30, paint);
        paint.clearShadowLayer();
        tvPaint.setColor(this.textColor);
        tvPaint.setTextSize(UIUtil.sp2px(context, 10));
        canvas.drawText(text, start, end, x + 12, y - 3, tvPaint);
    }
}