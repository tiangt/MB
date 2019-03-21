package com.whzl.mengbi.ui.widget.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

/**
 * @author nobody
 * @date 2019/3/6
 */
public class TipRadioButton extends android.support.v7.widget.AppCompatRadioButton {
    private Context context;

    private boolean isTipOn = false;

    private Dot mDot;

    private class Dot {

        int color;

        int radius;

        int marginTop;
        int marginRight;

        Dot() {
            float density = getContext().getResources().getDisplayMetrics().density;
            radius = (int) (2.5 * density);
            marginTop = 0;
            marginRight = 0;

            color = Color.parseColor("#ff2d4e");
        }

    }

    public boolean isTipOn() {
        return isTipOn;
    }

    public TipRadioButton(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public TipRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public TipRadioButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init();
    }

    private void init() {
        mDot = new Dot();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (isTipOn) {
            float cx = getWidth() - mDot.marginRight - mDot.radius;
            float cy = mDot.marginTop + mDot.radius;

            Drawable drawableTop = getCompoundDrawables()[1];
            if (drawableTop != null) {
                int drawableTopWidth = drawableTop.getIntrinsicWidth();
                if (drawableTopWidth > 0) {
                    int dotLeft = getWidth() / 2 + drawableTopWidth / 2;
                    cx = dotLeft + mDot.radius;
                }
            }

            Paint paint = getPaint();
            //save
            int tempColor = paint.getColor();

            paint.setColor(mDot.color);
            paint.setStyle(Paint.Style.FILL);
            canvas.drawCircle(cx + 2, cy, mDot.radius, paint);

            //restore
            paint.setColor(tempColor);
        }
    }

    public void setTipOn(boolean tip) {
        this.isTipOn = tip;
        invalidate(); // 重新绘制
    }
}
