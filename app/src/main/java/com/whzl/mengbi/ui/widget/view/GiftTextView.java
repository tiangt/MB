package com.whzl.mengbi.ui.widget.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

/**
 * @author nobody
 * @date 2019/5/28
 */
public class GiftTextView extends android.support.v7.widget.AppCompatTextView {
    public GiftTextView(Context context) {
        super(context);
    }

    public GiftTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GiftTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // draw the shadow
        getPaint().setShadowLayer(1, -2, 1, Color.BLACK); // or whatever shadow you use
        getPaint().setShader(null);
        super.onDraw(canvas);

        // draw the gradient filled text
        getPaint().clearShadowLayer();
        getPaint().setShader(new LinearGradient(0, getHeight(), 0, 0,
                Color.rgb(255, 90, 0), Color.rgb(255, 241, 78), Shader.TileMode.CLAMP));
        super.onDraw(canvas);
    }
}
