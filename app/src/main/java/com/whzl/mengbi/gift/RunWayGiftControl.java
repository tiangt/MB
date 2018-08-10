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
    private ArrayList<RunWayEvent> singleRunwayQueue = new ArrayList<>();
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
        if (event.getRunWayJson().getContext().isCacheIt()) {
            if (cacheEvent != null && !cacheEvent.isHasRuned()) {
                cacheEvent.getRunWayJson().getContext().setCacheIt(false);
                singleRunwayQueue.add(0, cacheEvent);
            }
            cacheEvent = event;
        }
        if (!autoScrollView.isStarting) {
            startRun(event);
            return;
        }
        if (!event.getRunWayJson().getContext().isCacheIt()) {
            singleRunwayQueue.add(0, event);
        }

    }

    private synchronized void startRun(RunWayEvent event) {
        if (autoScrollView == null) {
            return;
        }
        autoScrollView.setOnClickListener(v -> {
            if(listener != null){
                listener.onClick(event.getRunWayJson().getContext().getProgramId(), event.getRunWayJson().getContext().getNickname());
            }
        });
        autoScrollView.init(event, () -> {
            if (singleRunwayQueue.size() > 0) {
                RunWayGiftControl.this.startRun(singleRunwayQueue.get(0));
                singleRunwayQueue.remove(0);
            } else if (cacheEvent != null && !cacheEvent.equals(autoScrollView.getRunWayEvent())) {
                RunWayGiftControl.this.startRun(cacheEvent);
            }
        });
        autoScrollView.startScroll();
    }

    public void destroy() {
        autoScrollView = null;
        singleRunwayQueue = null;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    public void setListener(OnClickListener listener) {
        this.listener = listener;
    }

    public OnClickListener listener;

    public interface OnClickListener{
        void onClick(int programId, String nickname);
    }
}
