package com.whzl.mengbi.gift;

import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.whzl.mengbi.R;
import com.whzl.mengbi.chat.room.message.events.RunWayEvent;
import com.whzl.mengbi.ui.widget.view.AutoScrollTextView;
import com.whzl.mengbi.util.RxTimerUtil;

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
    private String type;

    public RunWayGiftControl(AutoScrollTextView autoScrollTextView) {
        this.autoScrollView = autoScrollTextView;
    }

    public RunWayGiftControl(AutoScrollTextView autoScrollTextView, FrameLayout frameLayout, ImageView imageView) {
        this.autoScrollView = autoScrollTextView;
        this.frameLayout = frameLayout;
        this.imageView = imageView;
    }

    public void load(RunWayEvent event, String net) {
        if (event == null) {
            return;
        }
        event.from = net;
        if (event.from.equals("net")) {
            cacheEvent = event;
            type = event.getRunwayBean().getContext().getRunwayType();
        } else {
            type = event.getRunWayJson().getContext().getRunWayType();
        }
        trackAnim = new TrackAnim(frameLayout, imageView);
        if (!autoScrollView.isStarting) {
            startRun(event);
        } else {
            runwayQueue.add(event);
        }
    }

    private synchronized void startRun(RunWayEvent event) {
        if (autoScrollView == null) {
            return;
        }
        if (event.from.equals("socket") && event.getRunWayJson().getContext().isCacheIt()) {
            cacheEvent = event;
        }
        autoScrollView.setOnClickListener(v -> {
            if (listener != null) {
                if (event.from.equals("socket")) {
                    listener.onClick(event.getRunWayJson().getContext().getProgramId(), event.getRunWayJson().getContext().getToNickname());
                } else {
                    listener.onClick(event.getRunwayBean().getContext().getProgramId(), event.getRunwayBean().getContext().getToNickname());
                }
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
        RxTimerUtil.timer(200, new RxTimerUtil.IRxNext() {
            @Override
            public void doNext(long number) {
                frameLayout.setVisibility(View.VISIBLE);
                if ("destroy".equals(type)) {
                    frameLayout.setBackgroundResource(R.drawable.shape_round_rect_supercar_capture);
                } else {
                    frameLayout.setBackgroundResource(R.drawable.shape_round_rect_supercar_board);
                }
                autoScrollView.setVisibility(View.VISIBLE);
                autoScrollView.setBackground(frameLayout);
                imageView.setVisibility(View.VISIBLE);
                trackAnim.startAnim();
            }
        });

    }

    public void destroy() {
        if (autoScrollView != null && trackAnim != null) {
            autoScrollView.stopScroll();
            autoScrollView.dispose();
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

