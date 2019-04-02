package com.whzl.mengbi.chat.room.message.messagesActions;

import android.content.Context;
import android.text.SpannableString;

import com.whzl.mengbi.chat.room.message.events.UpdatePubChatEvent;
import com.whzl.mengbi.chat.room.message.messageJson.GiftJson;
import com.whzl.mengbi.chat.room.message.messages.GiftMsg;
import com.whzl.mengbi.chat.room.util.DownloadEvent;
import com.whzl.mengbi.chat.room.util.DownloadImageFile;
import com.whzl.mengbi.chat.room.util.ImageUrl;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;


public class GiftAction implements Actions {
    @Override
    public void performAction(String msgStr, final Context context) {
        LogUtils.e("GiftAction  " + msgStr);
        GiftJson giftJson = GsonUtils.GsonToBean(msgStr, GiftJson.class);
        if (giftJson == null) {
            return;
        }
        int giftPicId = giftJson.getContext().getGoodsPicId();
        final String giftUrl = ImageUrl.getImageUrl(giftPicId, "jpg", giftJson.getContext().getLastUpdateTime());
        List<String> imageUrlList = new ArrayList<>();
        imageUrlList.add(giftUrl);
        DownloadImageFile downloadImageFile = new DownloadImageFile(new DownloadEvent() {
            @Override
            public void finished(List<SpannableString> imageSpanList) {
                SpannableString spanString = null;
                if (imageSpanList != null && !imageSpanList.isEmpty()) {
                    spanString = imageSpanList.get(0);
                }
                GiftMsg giftMsg = new GiftMsg(giftJson, context, spanString);
                EventBus.getDefault().post(new UpdatePubChatEvent(giftMsg));
            }
        });
        downloadImageFile.doDownload(imageUrlList, context);
    }
}
