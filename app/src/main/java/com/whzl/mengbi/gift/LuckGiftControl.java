package com.whzl.mengbi.gift;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.text.SpannableString;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.whzl.mengbi.chat.room.message.events.LuckGiftEvent;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.util.StringUtils;
import com.whzl.mengbi.util.UIUtil;

import java.util.ArrayList;

/**
 * @author shaw
 * @date 2018/8/3
 */
public class LuckGiftControl {

    private boolean isShow;
    private ArrayList<LuckGiftEvent> giftQueue = new ArrayList<>();

    private TextView tvLuckyGift;
    private final int screenWidthPixels;
    private ValueAnimator showAnim;
    private ValueAnimator hideAnim;

    public LuckGiftControl(TextView textView) {
        tvLuckyGift = textView;
        screenWidthPixels = UIUtil.getScreenWidthPixels(BaseApplication.getInstance());
    }

    public void load(LuckGiftEvent luckGiftEvent) {
        if (luckGiftEvent == null) {
            return;
        }
        if (!isShow) {
            show(luckGiftEvent);
            return;
        }
        giftQueue.add(luckGiftEvent);
    }

    private void show(LuckGiftEvent luckGiftEvent) {
        isShow = true;
        tvLuckyGift.setTranslationX(screenWidthPixels);
        tvLuckyGift.setText("恭喜");
        SpannableString nickNameSpannable = StringUtils.spannableStringColor(luckGiftEvent.getNickname(), Color.parseColor("#f76667"));
        tvLuckyGift.append(nickNameSpannable);
        tvLuckyGift.append("送幸运礼物喜中");
        SpannableString bonusCoinsSpannable = StringUtils.spannableStringColor(luckGiftEvent.getTotalLuckMengBi() + "", Color.parseColor("#f76667"));
        tvLuckyGift.append(bonusCoinsSpannable);
        tvLuckyGift.append("萌币");
        showTranslateAnim();
    }

    private void showTranslateAnim() {
        tvLuckyGift.setVisibility(View.VISIBLE);
        showAnim = ValueAnimator.ofFloat(screenWidthPixels, 0);
        showAnim.setInterpolator(new DecelerateInterpolator());
        showAnim.setDuration(1000);
        showAnim.addUpdateListener(animation -> {
            float animatedValue = ((float) animation.getAnimatedValue());
            tvLuckyGift.setTranslationX(animatedValue);
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
            tvLuckyGift.setTranslationX(animatedValue);
            if (animatedValue == -screenWidthPixels) {
                tvLuckyGift.setVisibility(View.GONE);
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
