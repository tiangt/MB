package com.whzl.mengbi.gift;

import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.events.RunWayEvent;
import com.whzl.mengbi.ui.widget.view.AutoScrollTextView;

import java.util.ArrayList;

/**
 * @author shaw
 * @date 2018/8/2
 */
public class RunWayGiftControl {
    private AutoScrollTextView autoScrollView;
    private FrameLayout frameLayout;
    private ImageView imageView;
    private ArrayList<RunWayEvent> runwayQueue = new ArrayList<>();
    private RunWayEvent cacheEvent;
    private TrackAnim trackAnim;

    public RunWayGiftControl(AutoScrollTextView autoScrollTextView) {
        this.autoScrollView = autoScrollTextView;
    }

    public RunWayGiftControl(AutoScrollTextView autoScrollTextView, FrameLayout frameLayout, ImageView imageView) {
        this.autoScrollView = autoScrollTextView;
        this.frameLayout = frameLayout;
        this.imageView = imageView;
    }

    public void load(RunWayEvent event) {
        if (event == null || event.getRunWayJson() == null
                || event.getRunWayJson().getContext() == null
                || autoScrollView == null) {
            return;
        }
        String type = event.getRunWayJson().getContext().getRunwayType();
        trackAnim = new TrackAnim(frameLayout, imageView);
        if (!autoScrollView.isStarting) {
            frameLayout.setVisibility(View.VISIBLE);
            if ("destroy".equals(type)) {
                frameLayout.setBackgroundResource(R.drawable.shape_round_rect_supercar_capture);
            } else if ("getOn".equals(type)) {
                frameLayout.setBackgroundResource(R.drawable.shape_round_rect_supercar_board);
            }
            autoScrollView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.VISIBLE);
            trackAnim.startAnim();
            trackAnim.setTrackAnimListener(new TrackAnim.OnTrackAnimListener() {
                @Override
                public void onAnimationEnd() {
                    startRun(event);
                }
            });
        }

        if(autoScrollView.isStarting){
            frameLayout.clearAnimation();
            frameLayout.setVisibility(View.VISIBLE);
            if ("destroy".equals(type)) {
                frameLayout.setBackgroundResource(R.drawable.shape_round_rect_supercar_capture);
            } else if ("getOn".equals(type)) {
                frameLayout.setBackgroundResource(R.drawable.shape_round_rect_supercar_board);
            }
            autoScrollView.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.VISIBLE);
            trackAnim.startAnim();
            trackAnim.setTrackAnimListener(new TrackAnim.OnTrackAnimListener() {
                @Override
                public void onAnimationEnd() {
                    startRun(event);
                }
            });
        }
        runwayQueue.add(event);
    }

    private synchronized void startRun(RunWayEvent event) {
        if (autoScrollView == null) {
            return;
        }
        if (event.getRunWayJson().getContext().isCacheIt()) {
            cacheEvent = event;
        }
        autoScrollView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onClick(event.getRunWayJson().getContext().getProgramId(), event.getRunWayJson().getContext().getToNickname());
            }
        });
        autoScrollView.init(event, () -> {
            if (runwayQueue.size() > 0) {
                startRun(runwayQueue.get(0));
                runwayQueue.remove(0);
            } else if (cacheEvent != null && !cacheEvent.equals(autoScrollView.getRunWayEvent())) {
                startRun(cacheEvent);
            }
        });
        autoScrollView.startScroll();
    }

    public void destroy() {
        if (autoScrollView != null) {
//            autoScrollView.stopScroll();
//            autoScrollView.dispose();
            trackAnim.stopAnim();
        }
        runwayQueue.clear();
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

