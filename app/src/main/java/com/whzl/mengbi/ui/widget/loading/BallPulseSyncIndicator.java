package com.whzl.mengbi.ui.widget.loading;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;

/**
 * Created by Jack on 2015/10/19.
 */
public class BallPulseSyncIndicator extends Indicator {

    float[] translateYFloats = new float[3];
    int[] colors = {Color.parseColor("#FF3030"),Color.parseColor("#87CEFF"),Color.parseColor("#FFD700")};

    @Override
    public void draw(Canvas canvas, Paint paint) {
        float circleSpacing = 10;
        float radius = (getWidth() - circleSpacing * 2) / 6;
        float x = getWidth() / 2 - (radius * 2 + circleSpacing);
        for (int i = 0; i < 3; i++) {
            canvas.save();
            float translateX = x + (radius * 2) * i + circleSpacing * i;
            paint.setColor(colors[i]);
            canvas.translate(translateX, translateYFloats[i]);
            canvas.drawCircle(0, 0, radius, paint);
            canvas.restore();
        }
    }

    @Override
    public ArrayList<ValueAnimator> onCreateAnimators() {
        ArrayList<ValueAnimator> animators = new ArrayList<>();
        float circleSpacing = 4;
        float radius = (getWidth() - circleSpacing * 2) / 6;
        int[] delays = new int[]{70, 140, 210};
        for (int i = 0; i < 3; i++) {
            final int index = i;
            ValueAnimator scaleAnim = ValueAnimator.ofFloat(getHeight() / 2, getHeight() / 2 - radius * 2, getHeight() / 2);
            scaleAnim.setDuration(800);
            scaleAnim.setRepeatCount(-1);
            scaleAnim.setStartDelay(delays[i]);
            addUpdateListener(scaleAnim, new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    translateYFloats[index] = (float) animation.getAnimatedValue();
                    postInvalidate();
                }
            });
            animators.add(scaleAnim);
        }
        return animators;
    }


}
