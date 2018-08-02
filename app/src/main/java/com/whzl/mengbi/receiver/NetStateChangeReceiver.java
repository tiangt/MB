package com.whzl.mengbi.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;

import com.whzl.mengbi.util.LogUtil;
import com.whzl.mengbi.util.zxing.NetUtils;

/**
 * @author shaw
 * @date 2018/8/1
 */
public class NetStateChangeReceiver extends BroadcastReceiver {
    private final static String TAG = "NetBroadcastReceiver";
    public NetEvevt evevt;

    @Override
    public void onReceive(final Context context, Intent intent) {
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            NetUtils utils = new NetUtils(context);
            int netWorkState = utils.getNetWorkState();
            LogUtil.d("shaw", netWorkState + "+++++++++++++++++++++++++==");
            if (evevt != null) {
                evevt.onNetChange(netWorkState);
            }
        }
    }

    // 自定义接口
    public interface NetEvevt {
        void onNetChange(int netMobile);
    }

    public void setEvevt(NetEvevt evevt) {
        this.evevt = evevt;
    }

}
