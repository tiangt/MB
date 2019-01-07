package com.whzl.mengbi.ui.adapter;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * @author nobody
 * @date 2019/1/4
 */
public class ChatMsgAnimation implements BaseAnimation {


    @Override
    public Animator[] getAnimators(View view) {
        return new Animator[]{
                ObjectAnimator.ofFloat(view.getRootView(), "alpha", 0.6f,1f),
        };
    }

}