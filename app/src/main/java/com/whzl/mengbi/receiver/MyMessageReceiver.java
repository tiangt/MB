package com.whzl.mengbi.receiver;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.sdk.android.push.MessageReceiver;
import com.alibaba.sdk.android.push.notification.CPushMessage;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.model.entity.ExtraMapBean;
import com.whzl.mengbi.ui.activity.LiveDisplayActivity;
import com.whzl.mengbi.ui.common.ActivityStackManager;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.SPUtils;

import java.util.Map;

/**
 * @author nobody
 * @date 2019-06-28
 */
public class MyMessageReceiver extends MessageReceiver {
    // 消息接收部分的LOG_TAG
    public static final String REC_TAG = "receiver";

    @Override
    public void onNotification(Context context, String title, String summary, Map<String, String> extraMap) {
        // TODO 处理推送通知
        Log.e("MyMessageReceiver", "Receive notification, title: " + title + ", summary: " + summary + ", extraMap: " + extraMap);
    }

    @Override
    public void onMessage(Context context, CPushMessage cPushMessage) {
        Log.e("MyMessageReceiver", "onMessage, messageId: " + cPushMessage.getMessageId() + ", title: " + cPushMessage.getTitle() + ", content:" + cPushMessage.getContent());
    }

    @Override
    public void onNotificationOpened(Context context, String title, String summary, String extraMap) {
//        if (ActivityStackManager.getInstance() == null) {
        Log.e("MyMessageReceiver", "onNotificationOpened, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap);
        ExtraMapBean extraMapBean = GsonUtils.GsonToBean(extraMap, ExtraMapBean.class);
        if (ActivityStackManager.getInstance() != null && !ActivityStackManager.getInstance().empty()) {
            Intent intent = new Intent(context, LiveDisplayActivity.class);
            intent.putExtra(LiveDisplayActivity.PROGRAMID, Integer.parseInt(extraMapBean.archives_id));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return;
        }
        if (extraMapBean != null && extraMapBean.archives_id != null && !TextUtils.isEmpty(extraMapBean.archives_id)) {
            SPUtils.put(context, SpConfig.PUSH_PROGRAMID, extraMapBean.archives_id);
        }
    }

    @Override
    protected void onNotificationClickedWithNoAction(Context context, String title, String summary, String extraMap) {
        Log.e("MyMessageReceiver", "onNotificationClickedWithNoAction, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap);
    }

    @Override
    protected void onNotificationReceivedInApp(Context context, String title, String summary, Map<String, String> extraMap, int openType, String openActivity, String openUrl) {
        Log.e("MyMessageReceiver", "onNotificationReceivedInApp, title: " + title + ", summary: " + summary + ", extraMap:" + extraMap + ", openType:" + openType + ", openActivity:" + openActivity + ", openUrl:" + openUrl);
    }

    @Override
    protected void onNotificationRemoved(Context context, String messageId) {
        Log.e("MyMessageReceiver", "onNotificationRemoved");
    }
}
