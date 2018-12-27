package com.whzl.mengbi.chat.room.message.messagesActions;


import android.content.Context;

import com.whzl.mengbi.util.LogUtils;

/**
 * @author cliang
 * @date 2018.12.27
 */
public class HeadlineAction implements Actions {
    @Override
    public void performAction(String msgStr, Context context) {
        LogUtils.e("Headline  ====== "+msgStr);
    }
}
