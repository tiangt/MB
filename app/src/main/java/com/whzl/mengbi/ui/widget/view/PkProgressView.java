package com.whzl.mengbi.ui.widget.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.ProgressBar;

import com.whzl.mengbi.R;


/**
 * @author cliang
 * @date 2018.11.8
 */
public class PkProgressView extends ProgressBar {

    private String mText;
    private int mDefaultPercent;
    private int mBackColorDefault;
    private int mBackColor;
    private int mTextColor;
    private int mTextSize;
    private Rect mBound;
    private int mTextAlignLeft;
    private Paint backPaint;
    private Paint barPaint;
    private int width;
    private int height;
    private RectF rectF;

    public PkProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PkProgressView(Context context) {
        this(context, null);
    }

    public PkProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    public String getText() {
        return mText;
    }

    public void setText(String mText) {
        this.mText = mText;
        postInvalidate();
    }

    public int getDefaultPercent() {
        return mDefaultPercent;
    }

    public void setDefaultPercent(int mDefaultPercent) {
        this.mDefaultPercent = mDefaultPercent;
        postInvalidate();
    }

    public int getBackColorDefault() {
        return mBackColorDefault;
    }

    public void setBackColorDefault(int mBackColorDefault) {
        this.mBackColorDefault = mBackColorDefault;
    }

    public int getBackColor() {
        return mBackColor;
    }

    public void setBackColor(int mBackColor) {
        this.mBackColor = mBackColor;
    }

    public int getTextColor() {
        return mTextColor;
    }

    public void setTextColor(int mTextColor) {
        this.mTextColor = mTextColor;
    }

    public int getTextSize() {
        return mTextSize;
    }

    public void setTextSize(int mTextSize) {
        this.mTextSize = mTextSize;
    }

    public int getTextAlignLeft() {
        return mTextAlignLeft;
    }

    public void setTextAlignLeft(int mTextAlignLeft) {
        this.mTextAlignLeft = mTextAlignLeft;
    }


    /**
     * @param context      上下文对象
     * @param attrs        参数
     * @param defStyleAttr
     */
    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PkProgressView, defStyleAttr, 0);
        int n = a.getIndexCount();
        for (int i = 0; i < n; i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.PkProgressView_backColor:
                    mBackColor = a.getColor(attr, Color.GRAY);
                    break;
                case R.styleable.PkProgressView_backColorDefault:
                    mBackColorDefault = a.getColor(attr, Color.WHITE);
                    break;
                case R.styleable.PkProgressView_defaultPercent:
                    mDefaultPercent = a.getInt(attr, 50);
                    break;
                case R.styleable.PkProgressView_text:
                    mText = a.getString(attr);
                    break;
                case R.styleable.PkProgressView_textSize:
                    mTextSize = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.PkProgressView_textColor:
                    mTextColor = a.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.PkProgressView_alignLeft:
                    mTextAlignLeft = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;
            }
        }
        a.recycle();
        backPaint = new Paint();
        backPaint.setColor(mBackColorDefault);
        backPaint.setStyle(Paint.Style.FILL);
        backPaint.setAntiAlias(true);

        barPaint = new Paint();
        barPaint.setColor(mBackColor);
        barPaint.setStyle(Paint.Style.FILL);
        barPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
    }

    @SuppressLint({"NewApi", "DrawAllocation"})
    @Override
    protected void onDraw(Canvas canvas) {
        // 绘制背景
        rectF = new RectF(0, 0, width, height);
        canvas.drawRoundRect(rectF, 18, 18, backPaint);
        // 测量字体
        mBound = new Rect();
        backPaint.getTextBounds(mText, 0, mText.length(), mBound);
        // 绘制已完成的进度
        Path path = new Path();
        path.moveTo(18, 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            path.addArc(0, 0, 36, 36, 180, 90);
        }
        if (mDefaultPercent < 0) {
            mDefaultPercent = 8;
        }
        if (mDefaultPercent > 100) {
            mDefaultPercent = 92;
        }
        if (mDefaultPercent >= 2 && mDefaultPercent <= 98) {
            // point2
            int p1_width = (mDefaultPercent - 2) * getWidth() / 100;
            path.lineTo(p1_width, 0);
            // point3
            int p3_width = (mDefaultPercent + 2) * getWidth() / 100;
            path.lineTo(p3_width, getHeight());
        } else if (mDefaultPercent < 10) {
            // point2
            int p1_width = (4) * getWidth() / 100;
            path.lineTo(p1_width, 0);
            // point3
            int p3_width = (8) * getWidth() / 100;
            path.lineTo(p3_width, getHeight());
        } else if (mDefaultPercent > 90) {
            // point2
            int p1_width = (92) * getWidth() / 100;
            path.lineTo(p1_width, 0);
            // point3
            int p3_width = (96) * getWidth() / 100;
            path.lineTo(p3_width, getHeight());
        }
        // point4
        path.lineTo(18, getHeight());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            path.addArc(0, getHeight() - 36, 36, getHeight(), 90, 90);
        }
        path.lineTo(0, 18);
        canvas.drawPath(path, barPaint);
    }
}
