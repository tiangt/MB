package com.whzl.mengbi.thread;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.whzl.mengbi.bean.TouristUserBean;
import com.whzl.mengbi.network.RequestManager;
import com.whzl.mengbi.network.URLContentUtils;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;


import java.util.HashMap;

public class TouristLoginThread extends Thread{

    private Handler mHandler;
    private Context mContext;
    private HashMap mHashMap;

    public TouristLoginThread(Context context, HashMap hashMap,Handler handler){
        this.mContext = context;
        this.mHashMap = hashMap;
        this.mHandler = handler;
    }

    @Override
    public void run() {
        super.run();
        //游客登录
            RequestManager.getInstance(mContext).requestAsyn(URLContentUtils.USER_VISITOR_LOGIN, RequestManager.TYPE_POST_JSON, mHashMap,
                    new RequestManager.ReqCallBack<Object>() {
                        @Override
                        public void onReqSuccess(Object result) {
                            String strJson = result.toString();
                            TouristUserBean mTouristUserBean = GsonUtils.GsonToBean(strJson,TouristUserBean.class);
                            if(mTouristUserBean.getCode()==200){
                                doTask(mTouristUserBean);
                            }
                        }

                        @Override
                        public void onReqFailed(String errorMsg) {
                            LogUtils.e("onReqFailed"+errorMsg.toString());
                        }
                    });
    }

    /**
     通过handler返回消息
     @param data
     */
    private void doTask(TouristUserBean data) {
        Message msg = Message.obtain();  //从全局池中返回一个message实例，避免多次创建message（如new Message）
        msg.obj = data;
        msg.what=1;   //标志消息的标志
        mHandler.sendMessage(msg);
    }
}
