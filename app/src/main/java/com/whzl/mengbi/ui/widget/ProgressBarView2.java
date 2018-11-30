package com.whzl.mengbi.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * @author cliang
 * @date 2018.11.30
 */
public class ProgressBarView2 extends View {

    //进度条最大值
    private float maxCount;
    //进度条当前值
    private float currentCount;
    private Paint mPaint;
    private int mWidth, mHeight;
    private String mText;
    private int mTextColor;
    private int mTextSize;

    public ProgressBarView2(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public ProgressBarView2(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProgressBarView2(Context context) {
        super(context);
    }

    /***
     * 设置最大的进度值
     *
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
     *
     * @param currentCount
     */
    public void setCurrentCount(float currentCount) {
        this.currentCount = currentCount > maxCount ? maxCount : currentCount;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint = new Paint();
        //设置抗锯齿效果
        mPaint.setAntiAlias(true);
        //设置画笔颜色
//        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.STROKE);//充满
        int round = mHeight / 2;
        RectF rf = new RectF(0, 0, mWidth, mHeight);
        /*绘制圆角矩形，背景色为画笔颜色*/
        canvas.drawRoundRect(rf, round, round, mPaint);
        /*设置progress内部是灰色*/
        mPaint.setColor(Color.rgb(211, 211, 211));
        RectF rectBlackBg = new RectF(2, 2, mWidth - 2, mHeight - 2);
        canvas.drawRoundRect(rectBlackBg, round, round, mPaint);
        //设置进度条进度及颜色
        float section = currentCount / maxCount;
        RectF rectProgressBg = new RectF(3, 3, (mWidth - 3) * section, mHeight - 3);
        if (section != 0.0f) {
            mPaint.setColor(Color.GREEN);
        } else {
            mPaint.setColor(Color.TRANSPARENT);
        }
        // 绘制文字
//        mPaint.setColor(mTextColor);
//        mPaint.setTextSize(mTextSize);
//        mPaint.setStrokeWidth(3);
//        Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
//        mPaint.setTextSize(20);
//        int baseline = (getMeasuredHeight() - fontMetrics.bottom + fontMetrics.top) / 2 - fontMetrics.top;
//        canvas.drawText(mText, 0, baseline, mPaint);
        canvas.drawRoundRect(rectProgressBg, round, round, mPaint);
    }

    //dip * scale + 0.5f * (dip >= 0 ? 1 : -1)
    private int dipToPx(int dip) {
        float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dip * scale + 0.5f * (dip >= 0 ? 1 : -1));//加0.5是为了四舍五入
    }

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
}
