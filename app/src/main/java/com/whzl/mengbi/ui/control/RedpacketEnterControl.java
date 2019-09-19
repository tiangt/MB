package com.whzl.mengbi.ui.control;

import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;

import com.whzl.mengbi.chat.room.message.events.RedpacketTotalEvent;
import com.whzl.mengbi.chat.room.message.events.UserRedpacketAwardEvent;
import com.whzl.mengbi.chat.room.message.events.UserRedpacketBroadEvent;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.ui.widget.view.RedpacketEnterView;
import com.whzl.mengbi.util.UIUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @author nobody
 * @date 2018/11/5
 */
public class RedpacketEnterControl {
    LinearLayout llEnter;
    RedpacketEnterView tvEnter;
    private final int screenWidthPixels;
    private final int enterWidth = UIUtil.dip2px(BaseApplication.getInstance(), 326);
    private ValueAnimator showAnim;
    private ValueAnimator hideAnim;

    public RedpacketEnterControl() {
        screenWidthPixels = UIUtil.getScreenWidthPixels(BaseApplication.getInstance());
    }


    Context context;

    public void setContext(Context context) {
        this.context = context;
    }

    public void setLlEnter(LinearLayout llEnter) {
        this.llEnter = llEnter;
    }

    public void setTvEnter(RedpacketEnterView tvEnter) {
        this.tvEnter = tvEnter;
    }

    private List<RedpacketTotalEvent> list = new ArrayList<>();

    private boolean isPlay = false;


    public void showEnter(RedpacketTotalEvent event) {
        list.add(event);
        if (!isPlay && list.size() != 0) {
            startAnimal();
        }

    }

    private void startAnimal() {
        isPlay = true;
        llEnter.setVisibility(View.VISIBLE);
        initTv(list.get(0));
        tvEnter.init();

        showAnim = ValueAnimator.ofFloat(screenWidthPixels, screenWidthPixels / 2 - enterWidth / 2);
        showAnim.setInterpolator(new DecelerateInterpolator());
        showAnim.setDuration(1000);
        showAnim.addUpdateListener(animation -> {
            float animatedValue = ((float) animation.getAnimatedValue());
            llEnter.setTranslationX(animatedValue);
            if (animatedValue == screenWidthPixels / 2 - enterWidth / 2) {
                tvEnter.startScroll();
                hideTranslateAnim();
            }
        });
        showAnim.start();
    }

    private void initTv(RedpacketTotalEvent event) {
        event.initTv(tvEnter);
        tvEnter.setOnClickListener(v -> {
            if (listener != null) {
                if (event instanceof UserRedpacketBroadEvent) {
                    listener.onClick(((UserRedpacketBroadEvent) event).json.context.uGameRedpacketDto.programId, ((UserRedpacketBroadEvent) event).json.context.anchorInfo.nickname);
                } else if (event instanceof UserRedpacketAwardEvent) {
                    listener.onClick(((UserRedpacketAwardEvent) event).json.context.uGameRedpacketDto.programId, ((UserRedpacketAwardEvent) event).json.context.userInfoDto.nickname);
                }
            }
        });
    }

    private void hideTranslateAnim() {
        hideAnim = ValueAnimator.ofFloat(screenWidthPixels / 2 - enterWidth / 2, -enterWidth);
        hideAnim.setInterpolator(new AccelerateInterpolator());
        hideAnim.setDuration(1000);
        hideAnim.addUpdateListener(animation -> {
            float animatedValue = ((float) animation.getAnimatedValue());
            llEnter.setTranslationX(animatedValue);
            if (animatedValue == -enterWidth) {
                llEnter.setVisibility(View.GONE);
                list.remove(0);
                tvEnter.stopScroll();
                tvEnter.setText("");
                if (list.size() > 0) {
                    startAnimal();
                } else {
                    isPlay = false;
                    llEnter.setVisibility(View.GONE);
                }
            }
        });
        hideAnim.setStartDelay(4000);
        hideAnim.start();
    }

    public void destroy() {
        isPlay = false;
        llEnter.setVisibility(View.GONE);
        if (tvEnter != null) {
            tvEnter.stopScroll();
            tvEnter.setText("");
        }
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
        list.clear();
    }

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    public OnClickListener listener;

    public interface OnClickListener {
        void onClick(int programId, String nickname);
    }
}
