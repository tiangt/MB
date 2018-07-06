package com.whzl.mengbi.ui.widget.view;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;


public class PubChatView extends RecyclerView {
    private Context context;
    private PubChatAdapter adapter;

    public PubChatView(Context context) {
        super(context);
        init(context);
    }

    public PubChatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PubChatView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        this.context = context;
        setHasFixedSize(false);
        LinearLayoutManager lm = new LinearLayoutManager(context);
        setLayoutManager(lm);
        adapter = new PubChatAdapter(this);

        setAdapter(adapter);
    }

    public void unregister() {
        adapter.unRegister();
    }

//    public void doBottomTask(final ToScrollCallBack cb) {
//        ((IChatRoom)context).mGetMainHandler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                int lastVisiblePosition = ((LinearLayoutManager) getLayoutManager()).findLastVisibleItemPosition();
//                if (lastVisiblePosition == NO_POSITION) {
//                    Log.i("test", "没有，按照 bottom");
//                    cb.atBottom();
//                } else if (adapter.getItemCount() - 1 - lastVisiblePosition <= 1) {
//                    Log.i("test", "在底部 " + "lastvisibleposition = " + lastVisiblePosition + "adapter count=" + adapter.getItemCount());
//                    cb.atBottom();
//                } else {
//                    Log.i("test", "不在底部 " + "lastvisibleposition = " + lastVisiblePosition + "adapter count=" + adapter.getItemCount());
//                    cb.notBottom();
//                }
//            }
//        },500);
//    }

    public void scrollToBottomWhenNot() {
//        if(((IChatRoom) context).getCurrentTabIndex() != 0){
//            Log.i("test","没有在公聊列表");
//            return;
//        }
//        doBottomTask(new ToScrollCallBack() {
//            @Override
//            public void atBottom() {
//                scrollToPosition(adapter.getItemCount() - 1);
//            }
//
//            @Override
//            public void notBottom() {
//
//            }
//        });
    }

    @Override
    public void onScrollStateChanged(int state) {
        Log.i("test", "滑动 " + state);
//        if (state == SCROLL_STATE_IDLE) {
//            doBottomTask(new ToScrollCallBack() {
//                @Override
//                public void atBottom() {
//                    Log.i("tset", "滑动时清空");
//                    ((IChatRoom) context).clearBottomMsg();
//                }
//
//                @Override
//                public void notBottom() {
//
//                }
//            });
//        }
    }

    public void scrollToBottomAnyWay() {
        if (adapter.getItemCount() == 0) {
            return;
        }
        invalidate();
        new Handler().postDelayed(new Runnable() {
            public void run() {
                scrollToPosition(adapter.getItemCount() - 1);//不能立即滑动
            }
        }, 500);
    }

}
