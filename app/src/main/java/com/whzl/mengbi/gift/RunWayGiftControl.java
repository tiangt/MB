package com.whzl.mengbi.gift;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.whzl.mengbi.chat.room.message.events.RunWayEvent;
import com.whzl.mengbi.ui.widget.view.AutoScrollTextView;

import java.util.ArrayList;

/**
 * @author shaw
 * @date 2018/8/2
 */
public class RunWayGiftControl {
    private AutoScrollTextView autoScrollView;
    private RelativeLayout relativeLayout;
    private ImageView imageView;
    private ArrayList<RunWayEvent> runwayQueue = new ArrayList<>();
    private RunWayEvent cacheEvent;
    private TrackAnim trackAnim;

    public RunWayGiftControl(AutoScrollTextView autoScrollTextView) {
        this.autoScrollView = autoScrollTextView;
    }

    public RunWayGiftControl(AutoScrollTextView autoScrollTextView, RelativeLayout relativeLayout, ImageView imageView) {
        this.autoScrollView = autoScrollTextView;
        this.relativeLayout = relativeLayout;
        this.imageView = imageView;
    }

    public void load(RunWayEvent event) {
        if (event == null || event.getRunWayJson() == null
                || event.getRunWayJson().getContext() == null
                || autoScrollView == null) {
            return;
        }
        if (!autoScrollView.isStarting) {
            relativeLayout.setVisibility(View.VISIBLE);
            imageView.setVisibility(View.VISIBLE);
            trackAnim = new TrackAnim(relativeLayout, imageView);
            trackAnim.startAnim();
            startRun(event);
            return;
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
            relativeLayout.setVisibility(View.GONE);
            imageView.setVisibility(View.GONE);
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

