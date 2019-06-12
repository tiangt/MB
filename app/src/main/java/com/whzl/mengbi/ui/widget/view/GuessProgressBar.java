package com.whzl.mengbi.ui.widget.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.ProgressBar;

import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.UIUtil;

/**
 * @author nobody
 * @date 2019-06-12
 */
public class GuessProgressBar extends ProgressBar {
    String text;
    Paint mPaint;
    Context context;

    public GuessProgressBar(Context context) {
        super(context);
        this.context = context;
        initText();
    }

    public GuessProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initText();
    }


    public GuessProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initText();
    }

    @Override
    public synchronized void setProgress(int progress) {
//        setText(progress);
        super.setProgress(progress);

    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //this.setText();
        Rect rect = new Rect();
        LogUtils.e("sssssssssssssss   "+getProgress());
        text = 100 - getProgress() + "%";
        this.mPaint.getTextBounds(text, 0, text.length(), rect);
//        int x = (getWidth() / 2) - rect.centerX();
        int y = (getHeight() / 2) - rect.centerY();
        canvas.drawText(getProgress() + "%", UIUtil.dip2px(context, 9), y, this.mPaint);
        canvas.drawText((100 - getProgress()) + "%", getWidth() - rect.width() - UIUtil.dip2px(context, 9), y, this.mPaint);
    }

    //初始化，画笔
    private void initText() {
        this.mPaint = new Paint();
        this.mPaint.setColor(Color.WHITE);
        this.mPaint.setTextSize(UIUtil.sp2px(context, 14));
        this.mPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
    }

}
