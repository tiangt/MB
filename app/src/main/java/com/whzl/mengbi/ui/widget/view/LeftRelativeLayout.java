package com.whzl.mengbi.ui.widget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.UIUtil;

/**
 * @author nobody
 * @date 2019/1/7
 */
public class LeftRelativeLayout extends RelativeLayout {
    private Context context;

    public void setLeftSlideListener(LeftSlideListener leftSlideListener) {
        this.leftSlideListener = leftSlideListener;
    }

    private LeftSlideListener leftSlideListener;

    public LeftRelativeLayout(Context context) {
        this(context, null);
        this.context = context;
    }

    public LeftRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        this.context = context;
    }

    public LeftRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    private float lastX;
    private float lastY;

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                lastX = event.getX();
                lastY = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                float newX = event.getX();
                float newY = event.getY();
                if (Math.abs(newX - lastX) > Math.abs(newY - lastY) && lastX - newX > UIUtil.dip2px(context, 90)) {
                    leftSlideListener.onLeftSlideListener();
                }
                break;

        }
        return super.dispatchTouchEvent(event);
    }

    public interface LeftSlideListener{
        void onLeftSlideListener();
    }
}
