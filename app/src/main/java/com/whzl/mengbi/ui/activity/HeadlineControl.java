package com.whzl.mengbi.ui.activity;

import android.animation.ValueAnimator;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.widget.view.HeadlineLayout;
import com.whzl.mengbi.util.UIUtil;

/**
 * @author cliang
 * @date 2018.12.24
 */
public class HeadlineControl {

    private boolean isShow;
    private final int screenWidthPixels;
    private HeadlineLayout mLayout;
    private ValueAnimator showAnim;
    private ValueAnimator hideAnim;

    public HeadlineControl(HeadlineLayout layout){
        mLayout = layout;
        screenWidthPixels = UIUtil.getScreenWidthPixels(BaseApplication.getInstance());
    }

    public void show(){
        isShow = true;
        mLayout.setTranslationX(screenWidthPixels);
        mLayout.getTextView().setText("11111111111111");
        showTranslateAnim();
    }

    private void showTranslateAnim() {
        mLayout.setVisibility(View.VISIBLE);
        showAnim = ValueAnimator.ofFloat(screenWidthPixels, 0);
        showAnim.setInterpolator(new DecelerateInterpolator());
        showAnim.setDuration(1000);
        showAnim.addUpdateListener(animation -> {
            float animatedValue = ((float) animation.getAnimatedValue());
            mLayout.setTranslationX(animatedValue);
            if (animatedValue == 0) {
                hideTranslateAnim();
            }
        });
        showAnim.start();
    }

    private void hideTranslateAnim() {
        hideAnim = ValueAnimator.ofFloat(0, -screenWidthPixels);
        hideAnim.setInterpolator(new AccelerateInterpolator());
        hideAnim.setDuration(1000);
        hideAnim.addUpdateListener(animation -> {
            float animatedValue = ((float) animation.getAnimatedValue());
            mLayout.setTranslationX(animatedValue);
            if (animatedValue == -screenWidthPixels) {
                mLayout.setVisibility(View.GONE);

            }
        });
        hideAnim.setStartDelay(3000);
        hideAnim.start();
    }

    public void destroy() {
        mLayout.setVisibility(View.GONE);
        if (showAnim != null) {
            showAnim.removeAllUpdateListeners();
            showAnim.cancel();
            showAnim.end();
            showAnim = null;
        }
        if (hideAnim != null) {
            hideAnim.removeAllUpdateListeners();
            hideAnim.cancel();
            hideAnim.end();
            hideAnim = null;
        }
    }
}
