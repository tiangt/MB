package com.whzl.mengbi.gift;

import android.animation.AnimatorSet;
import android.view.View;

/**
 *
 * @author shaw
 * @date 2017/7/7
 */

public interface ICustormAnim {
    AnimatorSet startAnim(GiftFrameLayout giftFrameLayout, View rootView);
    AnimatorSet comboAnim(GiftFrameLayout giftFrameLayout, View rootView, boolean isFirst);
    AnimatorSet endAnim(GiftFrameLayout giftFrameLayout, View rootView);
}
