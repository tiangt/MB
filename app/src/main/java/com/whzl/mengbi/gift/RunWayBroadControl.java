package com.whzl.mengbi.gift;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.constraint.ConstraintLayout;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;

import com.whzl.mengbi.chat.room.message.events.AnchorLevelChangeEvent;
import com.whzl.mengbi.chat.room.message.events.BroadCastBottomEvent;
import com.whzl.mengbi.chat.room.message.events.BroadEvent;
import com.whzl.mengbi.chat.room.message.events.LuckGiftBigEvent;
import com.whzl.mengbi.chat.room.message.events.RoyalLevelChangeEvent;
import com.whzl.mengbi.chat.room.message.events.UserLevelChangeEvent;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.widget.view.BroadTextView;
import com.whzl.mengbi.util.UIUtil;

import java.util.ArrayList;

/**
 * @author nobody
 * @date 2018/8/2
 */
public class RunWayBroadControl {
    private final ConstraintLayout clBottom;
    private final Context context;
    private BroadTextView autoScrollView;
    private ArrayList<BroadEvent> runwayQueue = new ArrayList<>();
    private int screenWidthPixels;
    private ValueAnimator showAnim;
    private ValueAnimator hideAnim;

    public RunWayBroadControl(Context context, BroadTextView autoScrollTextView, ConstraintLayout clBottom) {
        this.autoScrollView = autoScrollTextView;
        this.clBottom = clBottom;
        this.context = context;
        screenWidthPixels = UIUtil.getScreenWidthPixels(BaseApplication.getInstance());
    }

    public void load(BroadEvent event) {
        if (event instanceof UserLevelChangeEvent) {
            if (((UserLevelChangeEvent) event).getUserLeverChangeJson() == null || ((UserLevelChangeEvent) event).getUserLeverChangeJson().context == null || autoScrollView == null) {
                return;
            }
            if (!autoScrollView.isStarting) {
                startRun((UserLevelChangeEvent) event);
                return;
            }
            runwayQueue.add((UserLevelChangeEvent) event);
        } else if (event instanceof RoyalLevelChangeEvent) {
            if (((RoyalLevelChangeEvent) event).getRoyalLevelChangeJson() == null || ((RoyalLevelChangeEvent) event).getRoyalLevelChangeJson().context == null || autoScrollView == null) {
                return;
            }
            if (!autoScrollView.isStarting) {
                startRun((RoyalLevelChangeEvent) event);
                return;
            }
            runwayQueue.add((RoyalLevelChangeEvent) event);
        } else if (event instanceof AnchorLevelChangeEvent) {
            if (((AnchorLevelChangeEvent) event).getAnchorLevelChangeJson() == null || ((AnchorLevelChangeEvent) event).getAnchorLevelChangeJson().context == null || autoScrollView == null) {
                return;
            }
            if (!autoScrollView.isStarting) {
                startRun((AnchorLevelChangeEvent) event);
                return;
            }
            runwayQueue.add((AnchorLevelChangeEvent) event);
        } else if (event instanceof LuckGiftBigEvent) {
            if (((LuckGiftBigEvent) event).getLuckGiftBigJson() == null || ((LuckGiftBigEvent) event).getLuckGiftBigJson().context == null || autoScrollView == null) {
                return;
            }
            if (!autoScrollView.isStarting) {
                startRun((LuckGiftBigEvent) event);
                return;
            }
            runwayQueue.add((LuckGiftBigEvent) event);
        } else if (event instanceof BroadCastBottomEvent) {
            if (((BroadCastBottomEvent) event).getBroadCastBottomJson() == null || ((BroadCastBottomEvent) event).getBroadCastBottomJson().context == null || autoScrollView == null) {
                return;
            }
            if (!autoScrollView.isStarting) {
                startRun((BroadCastBottomEvent) event);
                return;
            }
            runwayQueue.add((BroadCastBottomEvent) event);
        }
//        else if (event instanceof PkEvent) {
//            if (((PkEvent) event).getmContext() == null || ((PkEvent) event).getPkJson() == null || autoScrollView == null) {
//                return;
//            }
//            if (!autoScrollView.isStarting) {
//                startRun((PkEvent) event);
//                return;
//            }
//            runwayQueue.add((PkEvent) event);
//        }
    }

    private synchronized void startRun(BroadEvent event) {
        if (autoScrollView == null) {
            return;
        }

        autoScrollView.setOnClickListener(v -> {
            if (listener != null) {
//                listener.onClick(event.getRunWayJson().getContext().getProgramId(), event.getRunWayJson().getContext().getToNickname());
            }
        });
        autoScrollView.init(event, () -> {
            if (runwayQueue.size() == 0) {
                autoScrollView.stopScroll();
                hideTranslateAnim();
            }
            if (runwayQueue.size() > 0) {
                keepRun(runwayQueue.get(0));
                runwayQueue.remove(0);
            }
        });

        startAnimal();
    }

    private synchronized void keepRun(BroadEvent event) {
        if (autoScrollView == null) {
            return;
        }

        autoScrollView.setOnClickListener(v -> {
            if (listener != null) {
//                listener.onClick(event.getRunWayJson().getContext().getProgramId(), event.getRunWayJson().getContext().getToNickname());
            }
        });
        autoScrollView.init(event, () -> {
            if (runwayQueue.size() == 0) {
                autoScrollView.stopScroll();
                hideTranslateAnim();
            }
            if (runwayQueue.size() > 0) {
                keepRun(runwayQueue.get(0));
                runwayQueue.remove(0);
            }
        });

        autoScrollView.startScroll();
    }

    private void startAnimal() {
        clBottom.setVisibility(View.VISIBLE);
        if (showAnim == null) {
            showAnim = ValueAnimator.ofFloat(screenWidthPixels, 0);
            showAnim.setInterpolator(new DecelerateInterpolator());
            showAnim.setDuration(1000);
            showAnim.addUpdateListener(animation -> {
                float animatedValue = ((float) animation.getAnimatedValue());
                clBottom.setTranslationX(animatedValue);
                if (animatedValue == 0) {
                    autoScrollView.startScroll();
                }
            });
        }
        showAnim.start();
    }

    private void hideTranslateAnim() {
        if (hideAnim == null) {
            hideAnim = ValueAnimator.ofFloat(0, -UIUtil.dip2px(context, 284));
            hideAnim.setInterpolator(new AccelerateInterpolator());
            hideAnim.setDuration(1000);
            hideAnim.addUpdateListener(animation -> {
                float animatedValue = ((float) animation.getAnimatedValue());
                clBottom.setTranslationX(animatedValue);
                if (animatedValue == -UIUtil.dip2px(context, 284)) {
                    clBottom.setVisibility(View.GONE);
                }
            });
        }
        hideAnim.start();
    }

    public void destroy() {
        if (autoScrollView != null) {
            autoScrollView.stopScroll();
            clBottom.setVisibility(View.GONE);
            autoScrollView.dispose();
        }
        runwayQueue.clear();
        if (showAnim != null) {
            showAnim.cancel();
            showAnim.end();
            showAnim = null;
        }
        if (hideAnim != null) {
            hideAnim.cancel();
            hideAnim.end();
            hideAnim = null;
        }
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    public OnClickListener listener;

    public interface OnClickListener {
        void onClick(int programId, String nickname);
    }
}
