package com.whzl.mengbi.chat.room.message.messagesActions;

import android.content.Context;

import com.whzl.mengbi.chat.room.message.messageJson.AnimJson;
import com.whzl.mengbi.chat.room.util.ImageUrl;
import com.whzl.mengbi.util.GsonUtils;


public class AnimAction implements Actions {
    @Override
    public void performAction(String msgStr, final Context context) {
        final AnimJson animJson = GsonUtils.GsonToBean(msgStr, AnimJson.class);
        if (animJson == null) {
            return;
        }

        final String giftUrl = ImageUrl.getImageUrl(animJson.getContext().getGoodsPicId(), ".jpg");
        final int giftCount = animJson.getContext().getCount();
        //TODO: dwownload the anim as bitmap
//        ImageRequest request = new ImageRequest(giftUrl, new Response.Listener<Bitmap>() {
//
//            @Override
//            public void onResponse(Bitmap response) {
//                Drawable picDrawable = new BitmapDrawable(response);
//
//                if (animJson.getAnimType().equals("PARABOLA")) {
//                    boolean isMyself = false;
//                    if (animJson.getContext().getUserId() == EnvironmentBridge.getUserInfoBridge().getUserId()) {//自己送的
//                        isMyself = true;
//                    }
//                    StarGiftEvent starGiftEvent = new StarGiftEvent(isMyself, picDrawable);
//                    EventBus.getDefault().post(starGiftEvent);
//                }
//
//                if (animJson.getAnimType().equals("PNG")) {
//                    String plistUrl = null;
//                    int plistRedId = -1;
//                    String pngUrl = null;
//                    int pngResId = -1;
//
//                    for (AnimJson.ResourcesEntity res : animJson.getResources()) {
//                        if (res.getResType().equals("PLIST")) {
//                            plistUrl = GiftPicUrlUtil.getPlistUrl(Integer.valueOf(res.getResValue()));
//                            plistRedId = res.getAnimationResId();
//                        } else if (res.getResType().equals("PNG")) {
//                            pngUrl = GiftPicUrlUtil.getPngUrl(Integer.valueOf(res.getResValue()));
//                            pngResId = res.getAnimationResId();
//                        }
//                    }
//
//                    EventBus.getDefault().post(new TexturePackerEvent(plistUrl, pngUrl, plistRedId, pngResId));
//                }
//
//                if (animJson.getAnimType().equals("DIFFUS")) {
//                    NormalGiftEvent normalGiftEvent = new NormalGiftEvent(giftCount, picDrawable);
//                    EventBus.getDefault().post(normalGiftEvent);
//                }
//            }
//        }, 0, 0, null, null);
//
//        VolleySingleton.getInstance(context).request(request);
    }
}
