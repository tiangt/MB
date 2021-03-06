package com.whzl.mengbi.chat.room.message.messagesActions;

import android.content.Context;
import android.util.Log;

import com.whzl.mengbi.chat.room.message.events.AnimEvent;
import com.whzl.mengbi.chat.room.message.messageJson.AnimJson;
import com.whzl.mengbi.chat.room.util.ImageUrl;
import com.whzl.mengbi.config.SpConfig;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.SPUtils;

import org.greenrobot.eventbus.EventBus;


public class AnimAction implements Actions {
    @Override
    public void performAction(String msgStr, final Context context) {
        Boolean giftEffect = (Boolean) SPUtils.get(context, SpConfig.GIFT_EFFECT, true);
        Boolean carEffect = (Boolean) SPUtils.get(context, SpConfig.CAR_EFFECT, true);
        Boolean comboEffect = (Boolean) SPUtils.get(context, SpConfig.COMBO_EFFECT, true);

        LogUtils.e("AnimAction  " + msgStr
        );
        final AnimJson animJson = GsonUtils.GsonToBean(msgStr, AnimJson.class);
        if (animJson == null) {
            return;
        }
        /** GIF 动画json
         /** GIF 动画json
         * {"msgType":"ANIMATION","animId":320,"resources":[{"animationResId":711,"location":"CENTRAL","resType":"PARAMS","resValue":"{\"playSeconds\":3.8}"},{"animationResId":710,"location":"CENTRAL","resType":"GIF","resValue":"164"}],"type":"busi.msg","animName":"首充座驾","platform":"MOBILE","animType":"MOBILE_CAR_GIF"}
         */
        String imageType;
        int resourceId;
        String aniType = animJson.getAnimType();

        if (("MOBILE_GIFT_GIF".equals(aniType) || "MOBILE_GIFT_SVGA".equals(aniType)) && !giftEffect) {
            return;
        }

        if (("MOBILE_CAR_GIF".equals(aniType) || "MOBILE_CAR_SVGA".equals(aniType)) && !carEffect) {
            return;
        }

        if (("TOTAl".equals(aniType) || "DIV".equals(aniType)) && !comboEffect) {
            return;
        }

        if (aniType.equals("MOBILE_GIFT_GIF") || aniType.equals("MOBILE_CAR_GIF")) {
            imageType = "gif";
            String strResId = "";
            for (AnimJson.ResourcesEntity resource : animJson.getResources()) {
                if (resource.getResType().equals("GIF")) {
                    strResId = resource.getResValue();
                    break;
                }
            }
            try {
                resourceId = Integer.valueOf(strResId);
            } catch (Exception e) {
                Log.e("chatMsg", "resourceId=" + strResId + " error");
                return;
            }
        } else if (aniType.equals("TOTAl") || "DIV".equals(aniType)) {
            imageType = "jpg";
            if (animJson.getContext() == null) {
                return;
            }
            resourceId = animJson.getContext().getGoodsPicId();
        } else if (aniType.equals("MOBILE_CAR_SVGA") || aniType.equals("MOBILE_GIFT_SVGA")) {
            imageType = "svga";
            String strResId = "";
            for (AnimJson.ResourcesEntity resource : animJson.getResources()) {
                if (resource.getResType().equals("SVGA")) {
                    strResId = resource.getResValue();
                    break;
                }
            }
            try {
                resourceId = Integer.valueOf(strResId);
            } catch (Exception e) {
                Log.e("chatMsg", "resourceId=" + strResId + " error");
                return;
            }
        } else {
            return;
        }
        String animUrl = ImageUrl.getImageUrl(resourceId, imageType);
        EventBus.getDefault().post(new AnimEvent(animJson, animUrl));
    }
}
