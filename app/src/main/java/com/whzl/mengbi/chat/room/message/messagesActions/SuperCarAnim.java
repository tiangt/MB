package com.whzl.mengbi.chat.room.message.messagesActions;

import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author cliang
 * @date 2018.11.1
 */
public class SuperCarAnim {

    private ImageView mImageView;
    private TextView mTextView;

    public SuperCarAnim(ImageView imageView, TextView textView) {
        this.mImageView = imageView;
        this.mTextView = textView;
    }

    public void startAnim() {
        //平移
        TranslateAnimation translate = new TranslateAnimation(
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
        translate.setInterpolator(new AnticipateInterpolator());
        translate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //渐变
                AlphaAnimation alpha = new AlphaAnimation(1.0f, 0f);
                alpha.setDuration(1000);
                alpha.setFillAfter(true);
                alpha.setInterpolator(new DecelerateInterpolator());
                mImageView.setAnimation(alpha);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mImageView.startAnimation(translate);
        mTextView.startAnimation(translate);
    }
}
