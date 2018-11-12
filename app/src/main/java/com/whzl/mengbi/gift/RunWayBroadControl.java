package com.whzl.mengbi.gift;

import com.whzl.mengbi.chat.room.message.events.AnchorLevelChangeEvent;
import com.whzl.mengbi.chat.room.message.events.BroadCastBottomEvent;
import com.whzl.mengbi.chat.room.message.events.BroadEvent;
import com.whzl.mengbi.chat.room.message.events.LuckGiftBigEvent;
import com.whzl.mengbi.chat.room.message.events.PkEvent;
import com.whzl.mengbi.chat.room.message.events.RoyalLevelChangeEvent;
import com.whzl.mengbi.chat.room.message.events.UserLevelChangeEvent;
import com.whzl.mengbi.ui.widget.view.AutoScrollTextView2;

import java.util.ArrayList;

/**
 * @author shaw
 * @date 2018/8/2
 */
public class RunWayBroadControl {
    private AutoScrollTextView2 autoScrollView;
    private ArrayList<BroadEvent> runwayQueue = new ArrayList<>();

    public RunWayBroadControl(AutoScrollTextView2 autoScrollTextView) {
        this.autoScrollView = autoScrollTextView;
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
        } else if (event instanceof PkEvent) {
            if (((PkEvent) event).getmContext() == null || ((PkEvent) event).getPkJson() == null || autoScrollView == null) {
                return;
            }
            if (!autoScrollView.isStarting) {
                startRun((PkEvent) event);
                return;
            }
            runwayQueue.add((PkEvent) event);
        }
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
            }
            if (runwayQueue.size() > 0) {
                startRun(runwayQueue.get(0));
                runwayQueue.remove(0);
            }
        });
        autoScrollView.startScroll();
    }

    public void destroy() {
        if (autoScrollView != null) {
            autoScrollView.stopScroll();
            autoScrollView.dispose();
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
