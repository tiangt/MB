package com.whzl.mengbi.ui.widget.view;

import android.content.Context;
import android.graphics.drawable.shapes.RoundRectShape;
import android.graphics.drawable.shapes.Shape;
import android.util.AttributeSet;
import android.widget.ProgressBar;

/**
 * @author cliang
 * @date 2018.12.10
 */
public class CustomProgressBar extends ProgressBar {

    public CustomProgressBar(Context context) {
        super(context);
    }

    public CustomProgressBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomProgressBar(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs, defStyle,0);

    }

    public CustomProgressBar(Context context, AttributeSet attrs, int defStyle, int styleRes) {

        super(context, attrs, defStyle);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec,
                                          int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public final int roundCorners = 15;
    Shape getDrawableShape() {
        final float[] roundedCorners = new float[] { 0, 0, 0, 0, 0, 0, 0, 0 };
        for(int i=0;i<roundedCorners.length;i++){
            roundedCorners[i] = dp2px(getContext(), roundCorners);
        }
        return new RoundRectShape(roundedCorners, null, null);
    }

    /**dpתpx*/
    public static float dp2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return dp * scale;
    }
}
