package com.whzl.mengbi.ui.widget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.FrameLayout;

/**
 * @author nobody
 * @date 2019-06-27
 */
public class ResizeFrameLayout extends FrameLayout {
    private KeyboardListener mListener;
    private int minKeyboardHeight;

    public interface KeyboardListener {
        void onKeyboardShown(int height);

        void onKeyboardHidden(int height);
    }

    public ResizeFrameLayout(Context context) {
        super(context);
    }

    public ResizeFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ResizeFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setKeyboardListener(KeyboardListener listener) {
        mListener = listener;
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        // 认为键盘最小高度为屏幕的1/4
        DisplayMetrics dm = getResources().getDisplayMetrics();
        minKeyboardHeight = dm.heightPixels / 4;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (h == 0 || oldh == 0) {
            return;
        }
        if (oldh - h > minKeyboardHeight) {
            notifyKeyboardEvent(true, oldh - h);
        } else if (h - oldh > minKeyboardHeight) {
            notifyKeyboardEvent(false, h - oldh);
        }
    }

    private void notifyKeyboardEvent(boolean show, int keyboardHeight) {
        if (mListener == null) {
            return;
        }
        if (show) {
            mListener.onKeyboardShown(keyboardHeight);
        } else {
            mListener.onKeyboardHidden(keyboardHeight);
        }
    }

}
