package com.whzl.mengbi.chat.room.message.messagesActions;

import android.content.Context;

import com.whzl.mengbi.util.LogUtils;

/**
 * @author nobody
 * @date 2018/12/3
 */
public class WeekStarAction implements Actions {
    @Override
    public void performAction(String msgStr, Context context) {
        LogUtils.e("sssssssssss  "+msgStr);
    }
}
