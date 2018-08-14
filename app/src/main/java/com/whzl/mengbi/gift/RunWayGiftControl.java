package com.whzl.mengbi.gift;

import com.whzl.mengbi.chat.room.message.events.RunWayEvent;
import com.whzl.mengbi.ui.widget.view.AutoScrollTextView;

import java.util.ArrayList;

/**
 * @author shaw
 * @date 2018/8/2
 */
public class RunWayGiftControl {
    private AutoScrollTextView autoScrollView;
    private ArrayList<RunWayEvent> runwayQueue = new ArrayList<>();
    private RunWayEvent cacheEvent;

    public RunWayGiftControl(AutoScrollTextView autoScrollTextView) {
        this.autoScrollView = autoScrollTextView;
    }

    public void load(RunWayEvent event) {
        if (event == null || event.getRunWayJson() == null
                || event.getRunWayJson().getContext() == null
                || autoScrollView == null) {
            return;
        }
        if (!autoScrollView.isStarting) {
            startRun(event);
            return;
        }
        runwayQueue.add(event);
    }

    private synchronized void startRun(RunWayEvent event) {
        if (autoScrollView == null) {
            return;
        }
        if(event.getRunWayJson().getContext().isCacheIt()){
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
        autoScrollView = null;
        runwayQueue = null;
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
