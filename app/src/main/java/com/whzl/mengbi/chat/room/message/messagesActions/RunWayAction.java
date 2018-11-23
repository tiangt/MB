package com.whzl.mengbi.chat.room.message.messagesActions;

import android.content.Context;
import android.text.SpannableString;
import android.util.Log;

import com.whzl.mengbi.chat.room.message.events.RunWayEvent;
import com.whzl.mengbi.chat.room.message.messageJson.RunWayJson;
import com.whzl.mengbi.chat.room.util.DownloadEvent;
import com.whzl.mengbi.chat.room.util.DownloadImageFile;
import com.whzl.mengbi.chat.room.util.ImageUrl;
import com.whzl.mengbi.util.GsonUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class RunWayAction implements Actions {
    @Override
    public void performAction(String msgStr, Context context) {
        Log.i("chen", "sssss = "+msgStr);
        RunWayJson runWayJson = GsonUtils.GsonToBean(msgStr, RunWayJson.class);
        if (null == runWayJson) {
            return;
        }
        int goodsPicId = runWayJson.getContext().getGoodsPicId();
        String goodsPicUrl = ImageUrl.getImageUrl(goodsPicId, "jpg");

        List<String> imageUrlList = new ArrayList<>();
        imageUrlList.add(goodsPicUrl);
        DownloadImageFile downloadImageFile = new DownloadImageFile(new DownloadEvent() {
            @Override
            public void finished(List<SpannableString> imageSpanList) {
                SpannableString spanString = null;
                if (imageSpanList != null && !imageSpanList.isEmpty()) {
                    spanString = imageSpanList.get(0);
                }
                EventBus.getDefault().post(new RunWayEvent(context, runWayJson, spanString));
            }
        });
        downloadImageFile.doDownload(imageUrlList, context);
    }
}
