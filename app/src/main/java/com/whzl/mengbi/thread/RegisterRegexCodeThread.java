package com.whzl.mengbi.thread;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.whzl.mengbi.network.RequestManager;
import com.whzl.mengbi.network.URLContentUtils;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.RegexUtils;
import com.whzl.mengbi.util.ToastUtils;

import java.util.HashMap;

public class RegisterRegexCodeThread extends Thread{

    private Context mContext;
    private HashMap mHashMap;
    private Handler mHandler;

    public RegisterRegexCodeThread(Context context,HashMap hashMap,Handler handler){
        this.mContext = context;
        this.mHashMap = hashMap;
        this.mHandler = handler;
    }

    @Override
    public void run() {
        super.run();
        RequestManager.getInstance(mContext).requestAsyn(URLContentUtils.SEND_CODE, RequestManager.TYPE_POST_JSON, mHashMap,
                new RequestManager.ReqCallBack<Object>() {
                    @Override
                    public void onReqSuccess(Object result) {
                        JSONObject jsonObject = JSON.parseObject(result.toString());
                        if(!jsonObject.get("code").equals(200)){
                            ToastUtils.showToast(jsonObject.get("msg").toString());
                            return;
                        }
                        doTask(jsonObject.get("data").toString());
                    }

                    @Override
                    public void onReqFailed(String errorMsg) {
                        LogUtils.d("errorMsg"+errorMsg.toString());
                    }
                });
    }

    private void doTask(String data){
        Message msg = Message.obtain();
        msg.obj = data;
        msg.what = 1;
        mHandler.sendMessage(msg);
    }
}
