package com.whzl.mengbi.thread;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.whzl.mengbi.bean.BannerBean;
import com.whzl.mengbi.network.RequestManager;
import com.whzl.mengbi.network.URLContentUtils;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;

import java.util.HashMap;

public class HomeBannerThread extends Thread{

    private Context mContext;
    private HashMap mHashMap;
    private Handler mHandler;

    public HomeBannerThread(Context context,HashMap hashMap,Handler handler){
        this.mContext = context;
        this.mHashMap = hashMap;
        this.mHandler = handler;
    }

    @Override
    public void run() {
        super.run();
            RequestManager.getInstance(mContext).requestAsyn(URLContentUtils.INDEX_ADV, RequestManager.TYPE_POST_JSON, mHashMap,
                    new RequestManager.ReqCallBack<Object>() {
                        @Override
                        public void onReqSuccess(Object result) {
                            BannerBean bannerBean = GsonUtils.GsonToBean(result.toString(), BannerBean.class);
                            if(bannerBean.getCode()==200){
                                doTask(bannerBean);
                            }
                        }

                        @Override
                        public void onReqFailed(String errorMsg) {
                            LogUtils.d("onReqFailed" + errorMsg.toString());
                        }
                    });
    }

    /**
     通过handler返回消息
     @param data
     */
    public void doTask(BannerBean data){
        Message msg = Message.obtain();//从全局池中返回一个message实例，避免多次创建message（如new Message）
        msg.obj = data;
        msg.what = 1;//标志消息的标志
        mHandler.sendMessage(msg);
    }
}
