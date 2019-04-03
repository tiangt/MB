package com.whzl.mengbi.gift;

import android.animation.ValueAnimator;
import android.graphics.Color;
import android.text.SpannableString;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import com.whzl.mengbi.chat.room.message.events.AnchorWishEndEvent;
import com.whzl.mengbi.chat.room.message.events.LuckGiftEvent;
import com.whzl.mengbi.chat.room.util.LightSpanString;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.util.StringUtils;
import com.whzl.mengbi.util.UIUtil;

import java.util.ArrayList;


/**
 * @author tt
 */
public class AnchorWishControl {

    private boolean isShow;
    private ArrayList<AnchorWishEndEvent> giftQueue = new ArrayList<>();

    private TextView tvLuckyGift;
    private final int screenWidthPixels;
    private ValueAnimator showAnim;
    private ValueAnimator hideAnim;

    public AnchorWishControl(TextView textView) {
        tvLuckyGift = textView;
        screenWidthPixels = UIUtil.getScreenWidthPixels(BaseApplication.getInstance());
    }

    public void load(AnchorWishEndEvent anchorWishEndEvent) {
        if (anchorWishEndEvent == null) {
            return;
        }
        if (!isShow) {
            show(anchorWishEndEvent);
            return;
        }
        giftQueue.add(anchorWishEndEvent);
    }

    private void show(AnchorWishEndEvent anchorWishEndEvent) {
        isShow = true;
        tvLuckyGift.setTranslationX(screenWidthPixels);
        tvLuckyGift.setText("恭喜 ");
        SpannableString nickNameSpannable = LightSpanString.getLightString(anchorWishEndEvent.anchorWishJson.context.nickName
                , Color.rgb(255, 247, 126));
        tvLuckyGift.append(nickNameSpannable);
        tvLuckyGift.append(" 完成心愿，获得 ");
        SpannableString bonusCoinsSpannable = LightSpanString.getLightString(
                anchorWishEndEvent.anchorWishJson.context.needGiftNumber + "个"
                        + anchorWishEndEvent.anchorWishJson.context.wishGiftName
                , Color.rgb(255,20,48));
        tvLuckyGift.append(bonusCoinsSpannable);
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
        tvLuckyGift.setVisibility(View.GONE);
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
