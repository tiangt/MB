package com.whzl.mengbi.gift;

import android.content.Context;
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
    private RxTimerUtil rxTimerUtil;
    private Context context;


    public RunWayGiftControl(Context context, AutoScrollTextView autoScrollTextView, FrameLayout frameLayout, ImageView imageView) {
        this.autoScrollView = autoScrollTextView;
        this.frameLayout = frameLayout;
        this.imageView = imageView;
        this.context = context;
        rxTimerUtil = new RxTimerUtil();
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
        if (autoScrollView != null && !autoScrollView.isStarting) {
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
                if (cacheEvent.from.equals("net")) {
                    type = cacheEvent.getRunwayBean().getContext().getRunwayType();
                } else {
                    type = cacheEvent.getRunWayJson().getContext().getRunWayType();
                }
                startRun(cacheEvent);
            }
        });
        autoScrollView.startScroll();
        rxTimerUtil.timer(200, new RxTimerUtil.IRxNext() {
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
        if (autoScrollView != null) {
            autoScrollView.stopScroll();
            autoScrollView.dispose();
        }
        if (trackAnim != null) {
            trackAnim.stopAnim();
            trackAnim = null;
        }
        imageView.setVisibility(View.GONE);
        rxTimerUtil.cancel();
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

