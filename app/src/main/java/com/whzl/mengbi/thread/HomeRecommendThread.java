package com.whzl.mengbi.thread;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.alibaba.fastjson.JSON;
import com.whzl.mengbi.model.entity.RecommendBean;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;
import com.whzl.mengbi.util.LogUtils;

import java.util.HashMap;

public class HomeRecommendThread extends  Thread{

    private Context mContext;
    private HashMap mHashMap;
    private Handler mHandler;

    public HomeRecommendThread(Context context,HashMap hashMap,Handler handler){
        this.mContext = context;
        this.mHashMap = hashMap;
        this.mHandler = handler;
    }

    @Override
    public void run() {
        super.run();
            RequestManager.getInstance(mContext).requestAsyn(URLContentUtils.RECOMMEND_ANCHOR, RequestManager.TYPE_POST_JSON, mHashMap,
                    new RequestManager.ReqCallBack<Object>() {
                        @Override
                        public void onReqSuccess(Object result) {
                            String jsonStr = result.toString();
                            RecommendBean  recommendBean = JSON.parseObject(jsonStr,RecommendBean.class);
                            if(recommendBean.getCode()==200){
                                doTask(recommendBean);
                            }
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
    public void doTask(RecommendBean data){
        Message msg = Message.obtain();//从全局池中返回一个message实例，避免多次创建message（如new Message）
        msg.obj = data;
        msg.what = 1;//标志消息的标志
        mHandler.sendMessage(msg);
    }
}
