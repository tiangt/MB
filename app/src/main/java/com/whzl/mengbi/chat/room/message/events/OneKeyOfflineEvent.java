package com.whzl.mengbi.chat.room.message.events;

import android.content.Context;

import com.whzl.mengbi.chat.room.message.messageJson.OneKeyOfflineJson;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.util.SPUtils;

/**
 * @author nobody
 * @date 2019/5/17
 */
public class OneKeyOfflineEvent {
    private final Context context;
    private final OneKeyOfflineJson oneKeyOfflineJson;

    public OneKeyOfflineEvent(Context context, OneKeyOfflineJson oneKeyOfflineJson) {
        this.context = context;
        this.oneKeyOfflineJson = oneKeyOfflineJson;
    }

    public boolean isInSession() {
        for (int i = 0; i < oneKeyOfflineJson.context.sessionList.size(); i++) {
            if (oneKeyOfflineJson.context.sessionList.get(i).sessionId.equals
                    (SPUtils.get(BaseApplication.getInstance(), SpConfig.KEY_SESSION_ID, "").toString())) {
                return true;
            }
        }
        return false;
    }
}
