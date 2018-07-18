package com.whzl.mengbi.chat.room.util;

import android.text.SpannableString;

import java.util.List;

/**
 * author: yaobo wu
 * date:   On 2018/7/18
 */
public interface DownloadEvent {
    public void finished(List<SpannableString> imageSpanList);
}
