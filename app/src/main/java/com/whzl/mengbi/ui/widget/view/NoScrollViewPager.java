package com.whzl.mengbi.ui.widget.view;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.whzl.mengbi.util.LogUtils;

/**
 * @author shaw
 * @date 03/12/2017
 */
public class NoScrollViewPager extends ViewPager {
    private boolean noScroll = true;

    int mLastMotionY=0;
    int mLastMotionX=0;

    public NoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public NoScrollViewPager(Context context) {
        super(context);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        int y = (int) e.getRawY();
        int x = (int) e.getRawX();
        boolean resume = false;
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 发生down事件时,记录y坐标
                mLastMotionY = y;
                mLastMotionX = x;
                resume = false;
                break;
            case MotionEvent.ACTION_MOVE:
                // deltaY > 0 是向下运动,< 0是向上运动
                int deltaY = y - mLastMotionY;
                int deleaX = x - mLastMotionX;

                if (Math.abs(deleaX) > Math.abs(deltaY)) {
                    resume = super.onInterceptTouchEvent(e);
                } else {
                    //当前正处于滑动
//                    if (isRefreshViewScroll(deltaY)) {
//                        resume = true;
//                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                resume = false;
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
        }
        return resume;
    }
}
