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
    private ArrayList<RunWayEvent> runwayCacheQueue = new ArrayList<>();
    private ArrayList<RunWayEvent> runwaySingleQueue = new ArrayList<>();

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
        if (event.getRunWayJson().getContext().isCacheIt()) {
            runwayCacheQueue.add(event);
        } else {
            runwaySingleQueue.add(event);
        }
    }

    private synchronized void startRun(RunWayEvent event) {
        if (autoScrollView == null) {
            return;
        }
        autoScrollView.init(event, () -> {
            if (runwayCacheQueue.size() > 0) {
                startRun(runwayCacheQueue.get(0));
                runwayCacheQueue.remove(0);
                return;
            }
            if (runwaySingleQueue.size() > 0) {
                startRun(runwaySingleQueue.get(0));
                runwaySingleQueue.remove(0);
            }
        });
        autoScrollView.startScroll();
    }

    public void destroy() {
        autoScrollView = null;
        runwayCacheQueue = null;
        runwaySingleQueue = null;
    }

}
