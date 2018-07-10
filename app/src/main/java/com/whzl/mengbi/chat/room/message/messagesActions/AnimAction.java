package com.whzl.mengbi.chat.room.message.messagesActions;

import android.content.Context;

import com.whzl.mengbi.chat.room.message.events.AnimEvent;
import com.whzl.mengbi.chat.room.message.messageJson.AnimJson;
import com.whzl.mengbi.chat.room.util.ImageUrl;
import com.whzl.mengbi.util.GsonUtils;

import org.greenrobot.eventbus.EventBus;


public class AnimAction implements Actions {
    @Override
    public void performAction(String msgStr, final Context context) {
        final AnimJson animJson = GsonUtils.GsonToBean(msgStr, AnimJson.class);
        if (animJson == null) {
            return;
        }

        String imageType;
        String aniType = animJson.getAnimType();
        if (aniType.equals("MOBILE_GIFT_GIF") || aniType.equals("MOBILE_CAR_GIF")) {
            imageType = ".gif";
        }else if (aniType.equals("TOTAl")) {
            imageType = ".jpg";
        }else {
            return;
        }
        String animUrl = ImageUrl.getImageUrl(animJson.getContext().getGoodsPicId(), imageType);
        EventBus.getDefault().post(new AnimEvent(animJson, animUrl));
    }
}
