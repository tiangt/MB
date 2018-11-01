package com.whzl.mengbi.chat.room.message.messagesActions;

import android.content.Context;
import android.text.SpannableString;
import android.text.TextUtils;

import com.whzl.mengbi.chat.room.message.events.UpdatePubChatEvent;
import com.whzl.mengbi.chat.room.message.messageJson.WelcomeJson;
import com.whzl.mengbi.chat.room.message.messages.WelcomeMsg;
import com.whzl.mengbi.chat.room.util.DownloadEvent;
import com.whzl.mengbi.chat.room.util.DownloadImageFile;
import com.whzl.mengbi.chat.room.util.ImageUrl;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;


public class WelComeAction implements Actions {
    @Override
    public void performAction(String msgStr, final Context context) {
        LogUtils.e2(msgStr);
        WelcomeJson welcomeJson = GsonUtils.GsonToBean(msgStr, WelcomeJson.class);
        if (null == welcomeJson||welcomeJson.getContext()==null||welcomeJson.getContext().getInfo()==null||welcomeJson.getContext().getInfo().getLevelList()==null) {
            return;
        }
        List<String> goodsUrlList = getGoodsList(welcomeJson.getContext().getInfo().getUserBagList());
        if (goodsUrlList.isEmpty()) {
            WelcomeMsg welMsg = new WelcomeMsg(welcomeJson, context, null);
            EventBus.getDefault().post(new UpdatePubChatEvent(welMsg));
            return;
        }
        DownloadImageFile downloadImageFile = new DownloadImageFile(new DownloadEvent() {
            @Override
            public void finished(List<SpannableString> imageSpanList) {
                WelcomeMsg welcomeMsg = new WelcomeMsg(welcomeJson, context, imageSpanList);
                UpdatePubChatEvent chatEvent = new UpdatePubChatEvent(welcomeMsg);
                EventBus.getDefault().post(chatEvent);
            }
        });
        downloadImageFile.doDownload(goodsUrlList, context);
    }

    private boolean getIsGuizu(List<WelcomeJson.WelcomeLevelListItem> levelList) {
        for (WelcomeJson.WelcomeLevelListItem item : levelList) {
            if (item.getLevelType().equals("ROYAL_LEVEL")) {
                if (item.getLevelValue() > 0) {
                    return true;
                }
            }
        }
        return false;
    }

    private int getGuizuLevel(List<WelcomeJson.WelcomeLevelListItem> levelList) {
        for (WelcomeJson.WelcomeLevelListItem item : levelList) {
            if (item.getLevelType().equals("ROYAL_LEVEL")) {
                return item.getLevelValue();
            }
        }
        return 1;
    }

    private int getUserLevel(List<WelcomeJson.WelcomeLevelListItem> levelList) {
        if (levelList == null) {
            return 1;
        }
        for (WelcomeJson.WelcomeLevelListItem item : levelList) {
            if (!TextUtils.isEmpty(item.getLevelType()) && item.getLevelType().equals("USER_LEVEL")) {
                return item.getLevelValue();
            }
        }
        return 1;
    }

    private List<String> getGoodsList(List<WelcomeJson.UserBagItem> bagList) {
        List<String> goodsUrlList = new ArrayList<>();
        if (null == bagList) {
            return goodsUrlList;
        }
        for (WelcomeJson.UserBagItem item : bagList) {
            if (item.getGoodsType().equals("BADGE")) {
                int goodId = item.getGoodsPicId();
                goodsUrlList.add(ImageUrl.getImageUrl(goodId, "jpg"));
            }
        }
        return goodsUrlList;
    }

}
