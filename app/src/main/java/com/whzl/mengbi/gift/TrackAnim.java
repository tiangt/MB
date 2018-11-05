package com.whzl.mengbi.gift;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * @author cliang
 * @date 2018.11.1
 */
public class TrackAnim {
    private FrameLayout mFrame;
    private ImageView mImageView;
    private OnTrackAnimListener mListener;
    private TranslateAnimation translate;
    private AlphaAnimation alpha;

    public TrackAnim(FrameLayout frameLayout, ImageView imageView) {
        this.mFrame = frameLayout;
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
                setTrackListener();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mImageView.startAnimation(translate);
        mFrame.startAnimation(translate);
    }

    public void stopAnim() {
        mImageView.clearAnimation();
        mFrame.clearAnimation();
    }

    public static interface OnTrackAnimListener {

        void onAnimationEnd();
    }

    public void setTrackAnimListener(OnTrackAnimListener listener) {
        this.mListener = listener;
    }

    private void setTrackListener(){
        alpha.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(mListener != null){
                    mListener.onAnimationEnd();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }
}
