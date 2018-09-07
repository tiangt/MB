package com.whzl.mengbi.gift;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.whzl.mengbi.chat.room.message.messageJson.AnimJson;

import java.util.ArrayList;

/**
 * @author shaw
 * @date 2018/7/30
 */

public class GiftControl implements LeftGiftAnimationStatusListener {

    private static final String TAG = "GiftControl";
    protected Context mContext;

    /**
     * 礼物队列
     */
    private ArrayList<AnimJson> mGiftQueue;

    /**
     * 添加礼物布局的父容器
     */
    private LinearLayout mGiftLayoutParent;
    /**
     * 最大礼物布局数
     */
    private int mGiftLayoutMaxNums;

    public GiftControl(Context context) {
        mContext = context;
        mGiftQueue = new ArrayList<>();
    }

    /**
     * @param giftLayoutParent 存放礼物控件的父容器
     * @param giftLayoutNums   礼物控件的数量
     * @return
     */
    public GiftControl setGiftLayout(LinearLayout giftLayoutParent, @NonNull int giftLayoutNums) {
        if (giftLayoutNums <= 0) {
            throw new IllegalArgumentException("GiftFrameLayout数量必须大于0");
        }
        mGiftLayoutParent = giftLayoutParent;
        mGiftLayoutMaxNums = giftLayoutNums;
        return this;
    }

    public void loadGift(AnimJson gift) {
        if (gift == null || gift.getContext() == null) {
            return;
        }
        AnimJson.ContextEntity contextEntity = gift.getContext();
        if ("TOTAl".equals(gift.getAnimType())) {
            if (mGiftLayoutParent.getChildCount() > 0) {
                for (int i = 0; i < mGiftLayoutParent.getChildCount(); i++) {
                    AnimGiftAction animGiftAction = (AnimGiftAction) mGiftLayoutParent.getChildAt(i);
                    if ("TOTAl".equals(animGiftAction.getAnimType())
                            && animGiftAction.getCurrentSendUserId() == contextEntity.getUserId()
                            && animGiftAction.getCurrentGiftId() == contextEntity.getGoodsId()) {
                        animGiftAction.setComboNum(contextEntity.getGiftTotalCount());
                        return;
                    }
                }
            }
            for (int i = 0; i < mGiftQueue.size(); i++) {
                AnimJson.ContextEntity context = mGiftQueue.get(i).getContext();
                if ("TOTAl".equals(mGiftQueue.get(i).getAnimType())
                        && context.getUserId() == contextEntity.getUserId()
                        && context.getGoodsId() == contextEntity.getGoodsId()
                        && context.getGiftTotalCount() < contextEntity.getGiftTotalCount()) {
                    context.getComboList().add(contextEntity.getGiftTotalCount());
                    return;
                }
            }
        } else if ("DIV".equals(gift.getAnimType())) {
            if (mGiftLayoutParent.getChildCount() > 0) {
                for (int i = 0; i < mGiftLayoutParent.getChildCount(); i++) {
                    AnimGiftAction animGiftAction = (AnimGiftAction) mGiftLayoutParent.getChildAt(i);
                    if ("DIV".equals(animGiftAction.getAnimType())
                            && animGiftAction.getCurrentSendUserId() == contextEntity.getUserId()
                            && animGiftAction.getCurrentGiftId() == contextEntity.getGoodsId()
                            && animGiftAction.getGiftCount() == contextEntity.getCount()) {
                        animGiftAction.setComboNum(contextEntity.getComboTimes());
                        return;
                    }
                }
            }
            for (int i = 0; i < mGiftQueue.size(); i++) {
                AnimJson.ContextEntity context = mGiftQueue.get(i).getContext();
                if ("DIV".equals(mGiftQueue.get(i).getAnimType())
                        && context.getUserId() == contextEntity.getUserId()
                        && context.getGoodsId() == contextEntity.getGoodsId()
                        && context.getCount() == contextEntity.getCount()
                        && context.getComboTimes() < contextEntity.getComboTimes()) {
                    context.getComboList().add(contextEntity.getComboTimes());
                    return;
                }
            }
        }
        gift.getContext().setComboList(new ArrayList<>());
        mGiftQueue.add(gift);
        showGift();
    }

    /**
     * 显示礼物
     */
    public synchronized void showGift() {
        int childCount = mGiftLayoutParent.getChildCount();
        Log.d(TAG, "showGift: 礼物布局的个数" + childCount);
        if (childCount < mGiftLayoutMaxNums) {
            //没有超过最大的礼物布局数量，可以继续添加礼物布局
            //两个参数分别是layout_width,layout_height
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mGiftLayoutParent.getLayoutParams();
            //这个就是添加其他属性的，这个是在父元素的底部。
            lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            if ("TOTAl".equals(mGiftQueue.get(0).getAnimType())) {
                GiftFrameLayout giftFrameLayout = new GiftFrameLayout(mContext);
                giftFrameLayout.setGiftAnimationListener(this);
                mGiftLayoutParent.addView(giftFrameLayout, 0);
                boolean hasGift = giftFrameLayout.setGift(getGift().getContext());
                giftFrameLayout.setAnimType("TOTAl");
                if (hasGift) {
                    giftFrameLayout.startAnimation();
                }
            } else if ("DIV".equals(mGiftQueue.get(0).getAnimType())) {
                ComboGiftFrameLayout comboGiftFrameLayout = new ComboGiftFrameLayout(mContext);
                comboGiftFrameLayout.setGiftAnimationListener(this);
                mGiftLayoutParent.addView(comboGiftFrameLayout, 0);
                boolean hasGift = comboGiftFrameLayout.setGift(getGift().getContext());
                comboGiftFrameLayout.setAnimType("DIV");
                if (hasGift) {
                    comboGiftFrameLayout.startAnimation();
                }
            }
            Log.d(TAG, "showGift: end->集合个数：" + mGiftQueue.size());
        }
    }

    /**
     * 取出礼物
     *
     * @return
     */
    private synchronized AnimJson getGift() {
        AnimJson gift = null;
        if (mGiftQueue.size() != 0) {
            gift = mGiftQueue.get(0);
            mGiftQueue.remove(0);
        }
        return gift;
    }


    @Override
    public void dismiss(AnimGiftAction animGiftAction) {
        reStartAnimation(animGiftAction);
    }

    private void reStartAnimation(final AnimGiftAction animGiftAction) {
        //动画结束，这时不能触发连击动画
        Log.d(TAG, "reStartAnimation: 动画结束");
        AnimatorSet animatorSet = animGiftAction.endAnmation();
        if (animatorSet != null) {
            animatorSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    animGiftAction.setGiftViewEndVisibility(isEmpty());
                    animGiftAction.clearHandler();
                    mGiftLayoutParent.removeView(((View) animGiftAction));
                    if (!isEmpty()) {
                        showGift();
                    }
                }
            });
        }
    }

    /**
     * 清除所有礼物
     */
    public synchronized void destroy() {
        if (mGiftQueue != null) {
            mGiftQueue.clear();
        }
        for (int i = 0; i < mGiftLayoutParent.getChildCount(); i++) {
            AnimGiftAction animGiftAction = (AnimGiftAction) mGiftLayoutParent.getChildAt(i);
            if (animGiftAction != null) {
                animGiftAction.clearHandler();
                animGiftAction.firstHideLayout();
            }
        }
        mGiftLayoutParent.removeAllViews();
    }

    public boolean isEmpty() {
        return mGiftQueue == null || mGiftQueue.size() == 0;
    }
}
