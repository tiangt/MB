package com.whzl.mengbi.gift;

import android.animation.AnimatorSet;

/**
 * @author shaw
 * @date 2018/8/21
 */
interface AnimGiftAction {
    void firstHideLayout();

    void setGiftViewEndVisibility(boolean hasGift);

    String getAnimType();

    void setAnimType(String mAnimType);

    void setComboNum(int comboNum);

    long getCurrentSendUserId();

    int getCurrentGiftId();

    int getGiftCount();

    AnimatorSet endAnmation();

    void clearHandler();
}
