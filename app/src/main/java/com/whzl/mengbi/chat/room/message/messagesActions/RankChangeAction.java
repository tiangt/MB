package com.whzl.mengbi.chat.room.message.messagesActions;

import android.content.Context;
import android.text.SpannableString;

import com.whzl.mengbi.chat.room.message.events.UpdatePubChatEvent;
import com.whzl.mengbi.chat.room.message.messageJson.RankChangeJson;
import com.whzl.mengbi.chat.room.message.messages.RankChangeMessage;
import com.whzl.mengbi.chat.room.util.DownloadEvent;
import com.whzl.mengbi.chat.room.util.DownloadImageFile;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author nobody
 * @date 2019/4/23
 */
public class RankChangeAction implements Actions {
    @Override
    public void performAction(String msgStr, Context context) {
        LogUtils.e("RankChangeAction   " + msgStr);
        RankChangeJson rankChangeJson = GsonUtils.GsonToBean(msgStr, RankChangeJson.class);
        if (rankChangeJson != null && rankChangeJson.context != null && rankChangeJson.context.msgInfoList != null && !rankChangeJson.context.msgInfoList.isEmpty()) {
            List<String> imageUrlList = new ArrayList<>();
            for (int i = 0; i < rankChangeJson.context.msgInfoList.size(); i++) {
                RankChangeJson.ContextBean.MsgInfoListBean msgInfoListBean = rankChangeJson.context.msgInfoList.get(i);
                if (msgInfoListBean.msgType.equals("IMG")) {
                    imageUrlList.add(msgInfoListBean.msgValue);
                }
            }

            DownloadImageFile downloadImageFile = new DownloadImageFile(new DownloadEvent() {
                @Override
                public void finished(List<SpannableString> imageSpanList) {
                    HashMap<String, SpannableString> hashMap = new HashMap<>();
                    if (imageSpanList != null && !imageSpanList.isEmpty()) {
                        for (int i = 0; i < imageSpanList.size(); i++) {
                            hashMap.put(imageSpanList.get(i).toString(), imageSpanList.get(i));
                        }
                    }
                    RankChangeMessage rankChangeMessage = new RankChangeMessage(context, rankChangeJson, hashMap);
                    EventBus.getDefault().post(new UpdatePubChatEvent(rankChangeMessage));
                }
            });
            downloadImageFile.doDownload(imageUrlList, context);

        }
    }
}
