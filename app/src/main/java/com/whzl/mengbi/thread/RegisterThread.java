package com.whzl.mengbi.thread;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.whzl.mengbi.model.entity.RegisterBean;
import com.whzl.mengbi.util.network.RequestManager;
import com.whzl.mengbi.util.network.URLContentUtils;
import com.whzl.mengbi.util.GsonUtils;

import java.util.HashMap;

public class RegisterThread extends Thread{

    private Context mContext;
    private HashMap mHashMap;
    private Handler mHandler;

    public RegisterThread(Context context,HashMap hashMap,Handler handler){
        this.mContext = context;
        this.mHashMap = hashMap;
        this.mHandler = handler;
    }

    @Override
    public void run() {
        super.run();

        RequestManager.getInstance(mContext).requestAsyn(URLContentUtils.REGISTER, RequestManager.TYPE_POST_JSON, mHashMap,
                new RequestManager.ReqCallBack<Object>() {
                    @Override
                    public void onReqSuccess(Object result) {
                        RegisterBean registerBean=  GsonUtils.GsonToBean(result.toString(),RegisterBean.class);
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {

                    }
                });
    }

    private void doTask(RegisterBean data){
        Message msg = Message.obtain();
        msg.obj = data;
        msg.what = 1;
        mHandler.sendMessage(msg);
    }

}
