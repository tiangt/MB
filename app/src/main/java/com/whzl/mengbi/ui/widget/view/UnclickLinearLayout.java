package com.whzl.mengbi.ui.widget.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.whzl.mengbi.util.ToastUtils;

/**
 * @author nobody
 * @date 2019/4/11
 */
public class UnclickLinearLayout extends LinearLayout {
    public UnclickLinearLayout(Context context) {
        super(context);
    }

    public UnclickLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public UnclickLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return false;
    }
}
