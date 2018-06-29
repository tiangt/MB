package com.whzl.mengbi.thread;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.whzl.mengbi.bean.LiveShowBean;
import com.whzl.mengbi.network.RequestManager;
import com.whzl.mengbi.network.URLContentUtils;
import com.whzl.mengbi.util.LogUtils;

import java.util.HashMap;

public class HomeLiveShowThread extends Thread{

    private Context mContext;
    private HashMap mHashMap;
    private Handler mHandler;

    public HomeLiveShowThread(Context context,HashMap hashMap,Handler handler){
        this.mContext = context;
        this.mHashMap = hashMap;
        this.mHandler = handler;
    }

    @Override
    public void run() {
        super.run();
                    RequestManager.getInstance(mContext).requestAsyn(URLContentUtils.SHOW_ANCHOR, RequestManager.TYPE_POST_JSON, mHashMap,
                new RequestManager.ReqCallBack<Object>() {
                    @Override
                    public void onReqSuccess(Object result) {
                      LiveShowBean  liveShowBean = JSON.parseObject(result.toString(), LiveShowBean.class);
                       doTask(liveShowBean);
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        LogUtils.e("onReqFailed"+errorMsg);
                    }
                });
    }

    /**
     通过handler返回消息
     @param data
     */
    public void doTask(LiveShowBean data){
        Message msg = Message.obtain();//从全局池中返回一个message实例，避免多次创建message（如new Message）
        msg.obj = data;
        msg.what = 1;//标志消息的标志
        mHandler.sendMessage(msg);
    }
}
