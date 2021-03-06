package com.whzl.mengbi.gift;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.messageJson.AnimJson;
import com.whzl.mengbi.chat.room.util.ImageUrl;
import com.whzl.mengbi.util.UIUtil;
import com.whzl.mengbi.util.glide.GlideImageLoader;

import java.util.ArrayList;


/**
 * @author shaw
 * @date 2018/7/30
 */
public class GiftFrameLayout extends FrameLayout implements Handler.Callback, AnimGiftAction {

    private static final String TAG = "GiftFrameLayout";
    private LayoutInflater mInflater;
    private Handler mHandler = new Handler(this);//连击handler
    private Handler comboHandler = new Handler(this);//检查连击handler
    private Runnable runnable;
    private ArrayList<Integer> comboList = new ArrayList<>();

    @Override
    public int getAnimType() {
        return mAnimType;
    }

    @Override
    public void setAnimType(int mAnimType) {
        this.mAnimType = mAnimType;
    }

    private int mAnimType;
    /**
     * 实时监测礼物数量
     */
    private static final int RESTART_GIFT_ANIMATION_CODE = 1002;
    /**
     * 礼物展示时间
     */
    public static final int GIFT_DISMISS_TIME = 3000;
    private static final int INTERVAL = 299;
    /**
     * 当前动画runnable
     */
    private Runnable mCurrentAnimRunnable;

    ImageView ivGift, ivAvatar;
    TextView tvFromNickName, tvCount, tvGiftName;

    private AnimJson.ContextEntity mGift;
    private Context context;
    /**
     * item 显示位置
     */

    /**
     * 是否开启礼物动画隐藏模式（如果两个礼物动画同时显示，并且第一个优先结束，第二个礼物的位置会移动到第一个位置上去）
     */
    private boolean isHideMode = false;

    private LeftGiftAnimationStatusListener mGiftAnimationListener;
    private View rootView;

    public GiftFrameLayout(Context context) {
        this(context, null);
    }

    public GiftFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mInflater = LayoutInflater.from(context);
        this.context = context;
        initView();
    }

    private void initView() {
        rootView = mInflater.inflate(R.layout.layout_gift_anim, this, true);
        ivGift = rootView.findViewById(R.id.iv_anim_gift_icon);
        ivAvatar = rootView.findViewById(R.id.iv_anim_gift_avatar);
        tvGiftName = rootView.findViewById(R.id.tv_anim_gift_name);
        tvFromNickName = rootView.findViewById(R.id.tv_anim_gift_form);
        tvCount = rootView.findViewById(R.id.tv_anim_gift_count);
        TextView tvX = rootView.findViewById(R.id.tv_x_gift_count);
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "FZSZJW.TTF");
        tvX.setTypeface(typeface, Typeface.ITALIC);
        tvCount.setTypeface(typeface, Typeface.ITALIC);
    }


    @Override
    public void firstHideLayout() {
        PropertyValuesHolder alpha = PropertyValuesHolder.ofFloat("alpha", 1.0f, 0f);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(GiftFrameLayout.this, alpha);
        animator.setStartDelay(0);
        animator.setDuration(0);
        animator.start();
    }

    public void hideView() {
        this.setVisibility(INVISIBLE);
    }

    @Override
    public void setGiftViewEndVisibility(boolean hasGift) {

        if (isHideMode && hasGift) {
            GiftFrameLayout.this.setVisibility(View.GONE);
        } else {
            GiftFrameLayout.this.setVisibility(View.INVISIBLE);
        }
    }

    public boolean setGift(AnimJson.ContextEntity gift) {
        if (gift == null) {
            return false;
        }
        mGift = gift;
        comboList.addAll(gift.getComboList());
        return true;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case RESTART_GIFT_ANIMATION_CODE:
                if (context != null) {
                    tvCount.setText(context.getString(R.string.live_gift_text, comboList.get(0), " "));
                }
                comboList.remove(0);
                comboAnimation(false);
                removeDismissGiftCallback();
                break;
            default:
                break;
        }
        return true;
    }

    /**
     * 显示完连击数与动画时,关闭此Item Layout,并通知外部隐藏自身(供内部调用)
     */
    private void dismissGiftLayout() {
        removeDismissGiftCallback();
        if (mGiftAnimationListener != null) {
            mGiftAnimationListener.dismiss(this);
        }
    }

    private void removeDismissGiftCallback() {
        stopCheckGiftCount();
        if (mCurrentAnimRunnable != null) {
            mHandler.removeCallbacks(mCurrentAnimRunnable);
            mCurrentAnimRunnable = null;
        }
        comboHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void setComboNum(int comboNum) {
        comboList.add(comboNum);
    }


    private class GiftNumAnimRunnable implements Runnable {

        @Override
        public void run() {
            dismissGiftLayout();
        }
    }

    /**
     * 设置item显示位置
     *
     * @param mIndex
     */

    /**
     * 获取ite显示位置
     *
     * @return
     */

    public void setGiftAnimationListener(LeftGiftAnimationStatusListener giftAnimationListener) {
        this.mGiftAnimationListener = giftAnimationListener;
    }

    /**
     * 获取当前显示礼物发送人id
     *
     * @return
     */
    @Override
    public long getCurrentSendUserId() {
        if (mGift != null) {
            return mGift.getUserId();
        }
        return -1;
    }

    /**
     * 获取当前显示礼物id
     *
     * @return
     */
    @Override
    public int getCurrentGiftId() {
        if (mGift != null) {
            return mGift.getGoodsId();
        }
        return -1;
    }

    @Override
    public int getGiftCount() {
        return 0;
    }

    private void checkGiftCountSubscribe() {
        runnable = () -> {
            if (comboList.size() > 0) {
                mHandler.sendEmptyMessage(RESTART_GIFT_ANIMATION_CODE);
            }
            comboHandler.postDelayed(runnable, INTERVAL);
        };
        comboHandler.postDelayed(runnable, INTERVAL);
    }

    public void stopCheckGiftCount() {
        comboHandler.removeCallbacksAndMessages(null);
    }

    @Override
    public void clearHandler() {
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;//这里要置位null，否则当前页面销毁时，正在执行的礼物动画会造成内存泄漏
        }

        mGiftAnimationListener = null;

        if (comboHandler != null) {
            comboHandler.removeCallbacksAndMessages(null);
            comboHandler = null;//这里要置位null，否则当前页面销毁时，正在执行的礼物动画会造成内存泄漏
        }
        resetGift();
    }

    public void resetGift() {
        runnable = null;
        mCurrentAnimRunnable = null;
        mGift = null;
        isHideMode = false;
    }

    /**
     * 动画开始时回调，使用方法借鉴{@link #startAnimation}
     */
    @SuppressLint("SetTextI18n")
    public void initLayoutState() {
        this.setVisibility(View.VISIBLE);
        if (context != null) {
            tvCount.setText(context.getString(R.string.live_gift_text, mGift.getGiftTotalCount(), " "));
        }
        tvFromNickName.setText(mGift.getNickname());
        tvGiftName.setText("送 ");
//        SpannableString spannableString = StringUtils.spannableStringColor(mGift.getGoodsName(), Color.parseColor("#fe4b21"));
        tvGiftName.append(mGift.getGoodsName());
        String avatarUrl = ImageUrl.getAvatarUrl(mGift.getUserId(), "jpg", mGift.getLastUpdateTime());
        GlideImageLoader.getInstace().displayImage(getContext(), avatarUrl, ivAvatar);
        GlideImageLoader.getInstace().displayImage(getContext(), mGift.getGiftUrl(), ivGift);
    }

    /**
     * 连击结束时回调
     */
    public void comboEndAnim() {
        if (mHandler != null) {
            if (comboList.size() > 0) {
                mHandler.sendEmptyMessage(RESTART_GIFT_ANIMATION_CODE);
            } else {
                mCurrentAnimRunnable = new GiftNumAnimRunnable();
                mHandler.postDelayed(mCurrentAnimRunnable, GIFT_DISMISS_TIME);
                checkGiftCountSubscribe();
            }
        }
    }

    public AnimatorSet startAnimation() {
        hideView();
//            布局飞入
        ObjectAnimator flyFromLtoR = GiftAnimationUtil.createFlyFromLtoR(rootView, -UIUtil.getScreenWidthPixels(getContext()), 0, 200, new DecelerateInterpolator());
        flyFromLtoR.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                initLayoutState();
            }
        });

        //礼物飞入
        ObjectAnimator flyFromLtoR2 = GiftAnimationUtil.createFlyFromLtoR(ivGift, 0, 0, 0, new DecelerateInterpolator());
        flyFromLtoR2.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
//                    ivGift.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                comboAnimation(true);
            }
        });
        AnimatorSet animatorSet = GiftAnimationUtil.startAnimation(flyFromLtoR, flyFromLtoR2);
        return animatorSet;
    }

    public void comboAnimation(boolean isFirst) {
        if (isFirst) {
            comboEndAnim();
        } else {
            //数量增加
            ObjectAnimator scaleGiftNum = GiftAnimationUtil.scaleGiftNum(tvCount);
            scaleGiftNum.setInterpolator(new LinearInterpolator());
            scaleGiftNum.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    comboEndAnim();
                }
            });
            scaleGiftNum.start();
        }
    }

    @Override
    public AnimatorSet endAnmation() {
        ObjectAnimator fadeAnimator = GiftAnimationUtil.createFadeAnimator(GiftFrameLayout.this, 0, -100, 300, 0);
        ObjectAnimator fadeAnimator2 = GiftAnimationUtil.createEndAnimator(GiftFrameLayout.this, 0, -20, 300, 0);

        AnimatorSet animatorSet = GiftAnimationUtil.startAnimationWith(fadeAnimator, fadeAnimator2);
        return animatorSet;
    }
}
