package com.whzl.mengbi.ui.widget.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ProgressBar;

/**
 * author: cliang
 * date: On 2018.12.3
 */
public class TextProgressBar extends ProgressBar {

    /**
     * 进度条最大值
     */
    private float maxCount;
    /**
     * 进度条当前值
     */
    private float currentCount;
    /**
     * 画笔
     */
    private Paint mPaint;

    /**
     * 文字
     */
    private String text;

    /**
     * 进度条颜色
     */
    private int percnetColor;

    private int mWidth, mHeight;
    private PorterDuffXfermode mPorterDuffXfermode;
    private Rect mRect;

    public TextProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public TextProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextProgressBar(Context context) {
        super(context);
        init();
    }

    /***
     * 设置最大的进度值
     * @param maxCount
     */
    public void setMaxCount(float maxCount) {
        this.maxCount = maxCount;
    }

    /**
     * 得到最大进度值
     */
    public double getMaxCount() {
        return maxCount;
    }


    /***
     * 设置当前的进度值
     * @param currentCount
     */
    public void setCurrentCount(float currentCount) {
        this.currentCount = currentCount > maxCount ? maxCount : currentCount;
        invalidate();
    }

    /**
     * 进度条颜色
     *
     * @param percentColor
     */
    public void setPercentColor(int percentColor) {
        this.percnetColor = percentColor;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        mPaint = new Paint();
        //设置抗锯齿效果
        mPaint.setAntiAlias(true);
        int round = mHeight / 2;
        /*设置progress内部是灰色*/
        mPaint.setColor(Color.rgb(211, 211, 211));
        RectF rectBlackBg = new RectF(2, 2, mWidth, mHeight);
        canvas.drawRoundRect(rectBlackBg, round, round, mPaint);
        //设置进度条进度及颜色
        float section = currentCount / maxCount;
        RectF rectProgressBg = new RectF(3, 3, (mWidth - 3) * section, mHeight - 3);
        if (section != 0.0f) {
            mPaint.setColor(percnetColor);
        } else {
            mPaint.setColor(Color.TRANSPARENT);
        }
        canvas.drawRoundRect(rectProgressBg, round, round, mPaint);
        mPaint.getTextBounds(text, 0, text.length(), mRect);
        mPaint.setColor(Color.WHITE);
        int x = (getWidth() / 2) - mRect.centerX();
        int y = (getHeight() / 2) - mRect.centerY();
        canvas.drawText(text, x, y, mPaint);
    }

    //dip * scale + 0.5f * (dip >= 0 ? 1 : -1)
    private int dipToPx(int dip) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));//加0.5是为了四舍五入
    }

    /**
     * 指定自定义控件在屏幕上的大小,onMeasure方法的两个参数是由上一层控件
     * 传入的大小，而且是模式和尺寸混合在一起的数值，需要MeasureSpec.getMode(widthMeasureSpec)
     * 得到模式，MeasureSpec.getSize(widthMeasureSpec)得到尺寸
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        //MeasureSpec.EXACTLY，精确尺寸
        if (widthSpecMode == MeasureSpec.EXACTLY || widthSpecMode == MeasureSpec.AT_MOST) {
            mWidth = widthSpecSize;
        } else {
            mWidth = 0;
        }
        //MeasureSpec.AT_MOST，最大尺寸，只要不超过父控件允许的最大尺寸即可，MeasureSpec.UNSPECIFIED未指定尺寸
        if (heightSpecMode == MeasureSpec.AT_MOST || heightSpecMode == MeasureSpec.UNSPECIFIED) {
            mHeight = dipToPx(20);
        } else {
            mHeight = heightSpecSize;
        }
        //设置控件实际大小
        setMeasuredDimension(mWidth, mHeight);
    }

    public void init() {
        mRect = new Rect();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPorterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_IN);
        mPaint.setTextSize(30);
        mPaint.setXfermode(null);
    }

}
