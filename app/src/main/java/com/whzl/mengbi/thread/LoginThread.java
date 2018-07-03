package com.whzl.mengbi.thread;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.whzl.mengbi.model.entity.UserInfo;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.ToastUtils;

import java.util.HashMap;


public class LoginThread extends Thread{

    private Handler mHandler;
    private Context mContext;
    private HashMap mHashMap;
    public LoginThread(Context context, HashMap hashMap,Handler handler){
        this.mContext = context;
        this.mHashMap = hashMap;
        this.mHandler = handler;
    }

    @Override
    public void run() {
        super.run();
        //用户登录
            RequestManager.getInstance(mContext).requestAsyn(URLContentUtils.USER_NORMAL_LOGIN, RequestManager.TYPE_POST_JSON, mHashMap,
                    new RequestManager.ReqCallBack<Object>() {
                        @Override
                        public void onReqSuccess(Object result) {
                            UserInfo userInfo=  GsonUtils.GsonToBean(result.toString(),UserInfo.class);
                            if(userInfo.getCode()==200){
                                doTask(userInfo);
                            }else{
                                ToastUtils.showToast(userInfo.getMsg());
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
    private void doTask(UserInfo data) {
        Message msg = Message.obtain();  //从全局池中返回一个message实例，避免多次创建message（如new Message）
        msg.obj = data;
        msg.what=1;   //标志消息的标志
        mHandler.sendMessage(msg);
    }
}
