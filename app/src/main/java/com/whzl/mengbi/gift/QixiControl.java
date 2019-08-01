package com.whzl.mengbi.gift;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.text.SpannableString;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.whzl.mengbi.chat.room.message.events.RankSuccessEvent;
import com.whzl.mengbi.chat.room.util.ImageUrl;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.util.StringUtils;
import com.whzl.mengbi.util.UIUtil;
import com.whzl.mengbi.util.glide.GlideImageLoader;

import java.util.ArrayList;


public class QixiControl {

    private final ConstraintLayout containerQixi;
    private final ImageView ivAnchorQixi;
    private final ImageView ivUserQixi;
    private final Context context;
    private boolean isShow;
    private ArrayList<RankSuccessEvent> giftQueue = new ArrayList<>();

    private TextView tvQixi;
    private final int screenWidthPixels;
    private ValueAnimator showAnim;
    private ValueAnimator hideAnim;

    public QixiControl(Context context, ConstraintLayout containerQixi, ImageView ivAnchorQixi, ImageView ivUserQixi, TextView textView) {
        tvQixi = textView;
        this.containerQixi = containerQixi;
        this.ivAnchorQixi = ivAnchorQixi;
        this.ivUserQixi = ivUserQixi;
        this.context = context;
        screenWidthPixels = UIUtil.getScreenWidthPixels(BaseApplication.getInstance());
    }

    public void load(RankSuccessEvent rankSuccessEvent) {
        if (rankSuccessEvent == null) {
            return;
        }
        if (!isShow) {
            show(rankSuccessEvent);
            return;
        }
        giftQueue.add(rankSuccessEvent);
    }

    private void show(RankSuccessEvent rankSuccessEvent) {
        isShow = true;
        containerQixi.setTranslationX(screenWidthPixels);
        tvQixi.setText("恭喜");
        SpannableString nickNameSpannable = StringUtils.spannableStringColor(rankSuccessEvent.rankSuccessJson.context.sendUserNickName, Color.rgb(12, 171, 254));
        tvQixi.append(nickNameSpannable);
        tvQixi.append("成为");
        SpannableString bonusCoinsSpannable = StringUtils.spannableStringColor(rankSuccessEvent.rankSuccessJson.context.nickName, Color.rgb(254, 25, 100));
        tvQixi.append(bonusCoinsSpannable);
        tvQixi.append("的最佳男友");
        GlideImageLoader.getInstace().displayCircleAvatar(context, ImageUrl.getAvatarUrl(rankSuccessEvent.rankSuccessJson.context.sendUserId,
                "jpg", rankSuccessEvent.rankSuccessJson.context.lastUpdateTime), ivUserQixi);
        GlideImageLoader.getInstace().displayCircleAvatar(context, ImageUrl.getAvatarUrl(rankSuccessEvent.rankSuccessJson.context.userId,
                "jpg", rankSuccessEvent.rankSuccessJson.context.lastUpdateTime), ivAnchorQixi);
        showTranslateAnim();
    }

    private void showTranslateAnim() {
        containerQixi.setVisibility(View.VISIBLE);
        showAnim = ValueAnimator.ofFloat(screenWidthPixels, 0);
        showAnim.setInterpolator(new DecelerateInterpolator());
        showAnim.setDuration(1000);
        showAnim.addUpdateListener(animation -> {
            float animatedValue = ((float) animation.getAnimatedValue());
            containerQixi.setTranslationX(animatedValue);
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
            containerQixi.setTranslationX(animatedValue);
            if (animatedValue == -screenWidthPixels) {
                containerQixi.setVisibility(View.GONE);
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
        containerQixi.setVisibility(View.GONE);
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
