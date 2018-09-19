package com.whzl.mengbi.ui.widget.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.graphics.Paint.Style;

/**
 * @author nobody
 * @date 2018/9/19
 */
public class AlignTextView extends View {
    private CharSequence text;
    // 宽高
    private int mWidth, mHeight;
    private float _density = 1;
    //文本画笔
    private Paint tPaint = null;
    //字体颜色
    private int textColor = Color.parseColor("#ff9806");
    //字体大小
    private float textSize = 13f;
    //画笔宽度
    private int strokeWidth = 2;
    //字数
    private int textCount;
    //每个字的宽度
    private float eachwidth;
    //字的y坐标
    private float textH;
    //偏移值
    private float offset;

    public AlignTextView(Context context) {
        super(context);
        init(context);
    }

    public AlignTextView(Context context, AttributeSet attrs,
                         int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    public AlignTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        _density = context.getResources().getDisplayMetrics().density;
        tPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        tPaint.setColor(textColor);
        tPaint.setStrokeWidth(strokeWidth);
        tPaint.setTextSize(textSize * _density);
        tPaint.setStyle(Style.FILL);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;

        if (this.text != null) {
            textCount = text.length();
            if (textCount <= 0) {
                return;
            }
            //文本总宽度
            float textWidth = tPaint.measureText(String.valueOf(text));
            Paint.FontMetrics metrics = tPaint.getFontMetrics();
            textH = (mHeight - (metrics.descent - metrics.ascent)) / 2 - metrics.ascent;
            //当文本总宽度大于控件的宽度,重新定义文字的尺寸
            if (textWidth > mWidth) {
                textSize = mWidth / textCount;
                tPaint.setTextSize(textSize);
                textWidth = mWidth;
            }
            //每个字的宽度
            eachwidth = textWidth / textCount;
            //偏移值，（宽度-字数的总宽度）/（字数-1）
            offset = (mWidth - textWidth) / (textCount - 1);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (this.text != null) {
            for (int i = 0; i < textCount; i++) {
                if (i == 0)//第一个字的位置
                {
                    canvas.drawText(String.valueOf(text.charAt(i)), offset * (i), textH, tPaint);
                } else if (i == textCount - 1)//最后个字的位置
                {
                    canvas.drawText(String.valueOf(text.charAt(i)), mWidth - eachwidth, textH, tPaint);
                } else {
                    canvas.drawText(String.valueOf(text.charAt(i)), (offset + eachwidth) * i, textH, tPaint);
                }

            }
        }
    }


    public CharSequence getText() {
        return text;
    }

    public void setText(CharSequence text) {
        this.text = text;
    }

    public int getTextColor() {
        return textColor;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
        this.tPaint.setColor(textColor);
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
        this.tPaint.setTextSize(textSize * _density);
    }
}
