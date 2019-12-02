package com.whzl.mengbi.ui.widget.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.whzl.mengbi.util.UIUtil;

/**
 * @author nobody
 * @date 2019-11-20
 */
public class PkRankView extends android.support.v7.widget.AppCompatImageView {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public PkRankView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    {
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(UIUtil.dip2px(getContext(), 1.5f));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        LinearGradient linearGradient = new LinearGradient(getHeight() / 2, 0, getHeight() / 2, getHeight(), Color.parseColor("#2DA8EE")
                , Color.parseColor("#FF2B3F"), Shader.TileMode.CLAMP);
        paint.setShader(linearGradient);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, getHeight() / 2 - UIUtil.dip2px(getContext(), 3), paint);
        super.onDraw(canvas);
    }
}
