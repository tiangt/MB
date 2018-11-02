package com.whzl.mengbi.gift;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * @author cliang
 * @date 2018.11.1
 */
public class TrackAnim {
    private RelativeLayout mRelative;
    private ImageView mImageView;
    private OnTrackAnimListener mListener;
    private TranslateAnimation translate;
    private AlphaAnimation alpha;

    public TrackAnim(RelativeLayout relative, ImageView imageView) {
        this.mRelative = relative;
        this.mImageView = imageView;
    }

    public void startAnim() {
        //平移
        translate = new TranslateAnimation(
                //X轴初始位置
                Animation.RELATIVE_TO_PARENT, 1.0f,
                //X轴移动的结束位置
                Animation.RELATIVE_TO_PARENT, -0.0f,
                //y轴开始位置
                Animation.RELATIVE_TO_PARENT, 0.0f,
                //y轴移动后的结束位置
                Animation.RELATIVE_TO_PARENT, 0.0f

        );
        translate.setFillAfter(true);
        translate.setDuration(2000);
        translate.setInterpolator(new DecelerateInterpolator());
        translate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //渐变
                alpha = new AlphaAnimation(1.0f, 0f);
                alpha.setDuration(1000);
                alpha.setFillAfter(true);
                mImageView.setAnimation(alpha);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mImageView.startAnimation(translate);
        mRelative.startAnimation(translate);
        if (translate.hasStarted() && mListener != null) {
            mListener.onAnimationStart(TrackAnim.this);
        }
        if (translate.hasEnded() && alpha.hasEnded() && mListener != null) {
            mListener.onAnimationEnd(TrackAnim.this);

        }
    }

    public void stopAnim() {
        mImageView.clearAnimation();
        mRelative.clearAnimation();
    }

    public static interface OnTrackAnimListener {
        void onAnimationStart(TrackAnim trackAnim);

        void onAnimationEnd(TrackAnim trackAnim);
    }

    public void setTrackAnimListener(OnTrackAnimListener listener) {
        this.mListener = listener;
    }
}
