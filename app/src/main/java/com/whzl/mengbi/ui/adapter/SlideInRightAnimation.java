package com.whzl.mengbi.ui.adapter;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * @author nobody
 * @date 2019/1/4
 */
public class SlideInRightAnimation implements BaseAnimation {


    @Override
    public Animator[] getAnimators(View view) {
        return new Animator[]{
//                ObjectAnimator.ofFloat(view, "translationX", view.getRootView().getWidth(), 0),
                ObjectAnimator.ofFloat(view.getRootView(), "scaleX", 1f, 1.06f,1f),
                ObjectAnimator.ofFloat(view.getRootView(), "scaleY", 1f, 1.06f,1f)
        };
    }

}