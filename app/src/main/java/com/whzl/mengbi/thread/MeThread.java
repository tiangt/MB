package com.whzl.mengbi.thread;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.whzl.mengbi.model.entity.UserInfo;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;

import java.util.HashMap;

public class MeThread extends Thread{

    private Context mContext;
    private HashMap mHashMap;
    private Handler mHandler;

    public MeThread(Context context,HashMap hashMap ,Handler handler){
        this.mContext = context;
        this.mHashMap = hashMap;
        this.mHandler = handler;
    }

    @Override
    public void run() {
        super.run();
            RequestManager.getInstance(mContext).requestAsyn(URLContentUtils.GET_USER_INFO,RequestManager.TYPE_POST_JSON,mHashMap,
                    new RequestManager.ReqCallBack(){
                        @Override
                        public void onReqSuccess(Object result) {
                            UserInfo userInfo = GsonUtils.GsonToBean(result.toString(),UserInfo.class);
                            if(userInfo.getCode()==200){
                                doTask(userInfo);
                            }
                        }

                        @Override
                        public void onReqFailed(String errorMsg) {
                            LogUtils.d("onReqFailed"+errorMsg);
                        }
                    });
    }

    /**
     通过handler返回消息
     @param data
     */
    private void doTask(UserInfo data) {
        Message msg = Message.obtain();  //从全局池中返回一个message实例，避免多次创建message（如new Message）
        msg.obj = data;
        msg.what=1;   //标志消息的标志
        mHandler.sendMessage(msg);
    }
}
