package com.whzl.mengbi.ui.widget.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;

/**
 * @author nobody
 * @date 2018/11/5
 */


public class RollTextView extends android.support.v7.widget.AppCompatTextView {
    private boolean aBoolean;
    public RollTextView(Context context) {
        super(context);
    }

    public RollTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RollTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

//    @Override
//    public boolean isFocused() {
//        return aBoolean;
//    }


    @Override
    public boolean isFocused() {
        return super.isFocused();
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
    }

    public void setFoucu(boolean bo) {
        aBoolean = bo;
    }
}
