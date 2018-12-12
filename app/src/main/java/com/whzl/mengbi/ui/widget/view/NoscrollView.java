package com.whzl.mengbi.ui.widget.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author nobody
 * @date 2018/12/12
 */
public class NoscrollView extends View {
    public NoscrollView(Context context) {
        super(context);
    }

    public NoscrollView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NoscrollView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                return true;
        }
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                return true;
        }
        return super.onTouchEvent(event);
    }
}
