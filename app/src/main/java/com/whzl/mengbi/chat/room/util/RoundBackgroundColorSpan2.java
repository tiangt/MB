package com.whzl.mengbi.chat.room.util;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.text.style.ReplacementSpan;

import com.whzl.mengbi.util.UIUtil;

import io.reactivex.annotations.NonNull;

/**
 * @author nobody
 * @date 2018/11/12
 */
public class RoundBackgroundColorSpan2 extends ReplacementSpan {
    private int mRadius;
    private int bgColor;
    private int textColor;
    private int mSize;
    private Context context;

    public RoundBackgroundColorSpan2(Context context, int bgColor,
                                     int textColor,
                                     int radius) {
        super();
        this.bgColor = bgColor;
        this.textColor = textColor;
        this.mRadius = radius;
        this.context = context;
    }

    /**
     * @param start 第一个字符的下标
     * @param end   最后一个字符的下标
     * @return span的宽度
     */
    @Override
    public int getSize(@NonNull Paint paint,
                       CharSequence text,
                       int start,
                       int end,
                       Paint.FontMetricsInt fm) {
        mSize = (int) (paint.measureText(text, start, end) + 2 * mRadius);
        return mSize + 5;//5:距离其他文字的空白
    }

    /**
     * @param y baseline
     */
    @Override
    public void draw(@NonNull Canvas canvas,
                     CharSequence text,
                     int start,
                     int end,
                     float x,
                     int top,
                     int y, int bottom,
                     @NonNull Paint paint) {
        int defaultColor = paint.getColor();//保存文字颜色
        float defaultStrokeWidth = paint.getStrokeWidth();
        //绘制圆角矩形
        paint.setColor(bgColor);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(UIUtil.dip2px(context, 1));
        paint.setAntiAlias(true);
        RectF rectF = new RectF(x + 2.5f, y + 2.5f + paint.ascent(), x + mSize, y + paint.descent());
        //设置文字背景矩形，x为span其实左上角相对整个TextView的x值，y为span左上角相对整个View的y值。
        // paint.ascent()获得文字上边缘，paint.descent()获得文字下边缘
        //x+2.5f解决线条粗细不一致问题
//        paint.setShader(new LinearGradient(x + 2.5f, y + 2.5f + paint.ascent(), x + mSize, y + paint.descent(), Color.RED,Color.BLUE, Shader.TileMode.CLAMP));

        canvas.drawRect(rectF, paint);

//        paint.setShader(null);
        //绘制文字
        paint.setColor(textColor);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(defaultStrokeWidth);
        canvas.drawText(text, start, end, x + mRadius, y, paint);//此处mRadius为文字右移距离


        paint.setColor(defaultColor);//恢复画笔的文字颜色
    }
}
