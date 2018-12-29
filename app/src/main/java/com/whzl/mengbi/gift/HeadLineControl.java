package com.whzl.mengbi.gift;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.text.SpannableString;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.events.HeadLineEvent;
import com.whzl.mengbi.chat.room.message.events.LuckGiftEvent;
import com.whzl.mengbi.chat.room.util.LightSpanString;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.widget.view.HeadlineLayout;
import com.whzl.mengbi.util.StringUtils;
import com.whzl.mengbi.util.UIUtil;

import java.util.ArrayList;

/**
 * @author nobody
 * @date 2018/12/29
 */
public class HeadLineControl {
    private boolean isShow;
    private ArrayList<HeadLineEvent> giftQueue = new ArrayList<>();

    private HeadlineLayout headlineLayout;
    private final int screenWidthPixels;
    private ValueAnimator showAnim;
    private ValueAnimator hideAnim;

    public HeadLineControl(HeadlineLayout headlineLayout) {
        this.headlineLayout = headlineLayout;
        screenWidthPixels = UIUtil.getScreenWidthPixels(BaseApplication.getInstance());
    }

    public void load(HeadLineEvent headLineEvent) {
        if (headLineEvent == null) {
            return;
        }
        if (!isShow) {
            show(headLineEvent);
            return;
        }
        giftQueue.add(headLineEvent);
    }

    private void show(HeadLineEvent headLineEvent) {
        isShow = true;
        headlineLayout.setTranslationX(screenWidthPixels);
        headlineLayout.tvNameHeadline.setText(headLineEvent.headLineJson.context.nickname);
        headlineLayout.tvNumHeadline.setText(headLineEvent.headLineJson.context.rank + "");
        showTranslateAnim();
    }

    private void showTranslateAnim() {
        headlineLayout.setVisibility(View.VISIBLE);
        showAnim = ValueAnimator.ofFloat(screenWidthPixels, 0);
        showAnim.setInterpolator(new DecelerateInterpolator());
        showAnim.setDuration(1000);
        showAnim.addUpdateListener(animation -> {
            float animatedValue = ((float) animation.getAnimatedValue());
            headlineLayout.setTranslationX(animatedValue);
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
            headlineLayout.setTranslationX(animatedValue);
            if (animatedValue == -screenWidthPixels) {
                headlineLayout.setVisibility(View.GONE);
                if (giftQueue.size() > 0) {
                    show(giftQueue.get(0));
                    giftQueue.remove(0);
                } else {
                    isShow = false;
                }
            }
        });
        hideAnim.setStartDelay(3000);
        hideAnim.start();
    }

    public void destroy() {
        headlineLayout.setVisibility(View.GONE);
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
        giftQueue.clear();
    }
}
