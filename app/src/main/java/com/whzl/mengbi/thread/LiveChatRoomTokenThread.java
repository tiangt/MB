package com.whzl.mengbi.thread;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.whzl.mengbi.bean.LiveRoomTokenBean;
import com.whzl.mengbi.network.RequestManager;
import com.whzl.mengbi.network.URLContentUtils;
import com.whzl.mengbi.util.GsonUtils;

import java.util.HashMap;

public class LiveChatRoomTokenThread extends Thread{

    private Context mContext;
    private HashMap mHashMap;
    private Handler mHandler;

    public LiveChatRoomTokenThread(Context context, HashMap hashMap, Handler handler){
        this.mContext = context;
        this.mHashMap = hashMap;
        this.mHandler = handler;
    }

    @Override
    public void run() {
        super.run();
        RequestManager.getInstance(mContext).requestAsyn(URLContentUtils.CHECK_ENTERR_ROOM, RequestManager.TYPE_POST_JSON, mHashMap, new RequestManager.ReqCallBack<Object>() {
            @Override
            public void onReqSuccess(Object result) {
                LiveRoomTokenBean liveRoomTokenBean = GsonUtils.GsonToBean(result.toString(),LiveRoomTokenBean.class);
                if(liveRoomTokenBean.getCode()==200){
                    doTask(liveRoomTokenBean);
                }
            }

            @Override
            public void onReqFailed(String errorMsg) {

            }
        });
    }

    public void doTask(LiveRoomTokenBean data){
        Message msg = Message.obtain();
        msg.obj = data;
        msg.what = 1;
        mHandler.sendMessage(msg);
    }
}
