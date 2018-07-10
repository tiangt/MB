package com.whzl.mengbi.ui.widget.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.whzl.mengbi.R;


/**
 * @author shaw
 * @date 2018/7/9
 */
public class RatioRelativeLayout extends RelativeLayout {

    private float mPicRatio;//图片的宽高比
    public static final int RELATIVE_WIDTH = 0;//控件的宽度固定，根据比例求出高度
    public static final int RELATIVE_HEIGHT = 1;//控件的高度固定，根据比例求出宽度
    private int mRelative = RELATIVE_WIDTH;


    public RatioRelativeLayout(Context context) {
        this(context, null);
    }

    public RatioRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RatioRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.RatioLayout);
        for (int i = 0; i < array.getIndexCount(); i++) {
            switch (array.getIndex(i)) {
                case R.styleable.RatioLayout_ratio:
                    mPicRatio = array.getFloat(i, 2f);
                    break;
                case R.styleable.RatioLayout_relative:
                    mRelative = array.getInt(i, 0);
                    break;
            }
        }
        array.recycle();
    }

    public void setPicRatio(float picRatio) {
        mPicRatio = picRatio;
    }

    public void setRelative(int relative) {
        mRelative = relative;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == MeasureSpec.EXACTLY && mPicRatio != 0 && mRelative == RELATIVE_WIDTH) {
            //修正高度的值
            heightSize = (int) (widthSize / mPicRatio + 0.5f);
        } else if (heightMode == MeasureSpec.EXACTLY && mPicRatio != 0 && mRelative ==
                RELATIVE_HEIGHT) {
            //修正宽度的值
            widthSize = (int) (heightSize * mPicRatio + 0.5f);
        }
        widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
