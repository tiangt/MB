package com.whzl.mengbi.ui.widget.view;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.style.ReplacementSpan;

/**
 * @author nobody
 * @date 2018/9/29
 */
public class RoundBackgroundColorSpan extends ReplacementSpan {
    private int bgColor;
    private int textColor;
    public RoundBackgroundColorSpan(int bgColor, int textColor) {
        super();
        this.bgColor = bgColor;
        this.textColor = textColor;
    }
    @Override
    public int getSize(Paint paint, CharSequence text, int start, int end, Paint.FontMetricsInt fm) {
        return ((int)paint.measureText(text, start, end)+60);
    }

    @Override
    public void draw(Canvas canvas, CharSequence text, int start, int end, float x, int top, int y, int bottom, Paint paint) {
        int color1 = paint.getColor();
        paint.setColor(this.bgColor);
        canvas.drawRoundRect(new RectF(x, top+1, x + ((int) paint.measureText(text, start, end)+40), bottom-1), 15, 15, paint);
        paint.setColor(this.textColor);
        canvas.drawText(text, start, end, x+20, y, paint);
        paint.setColor(color1);
    }
}
