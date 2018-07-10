package com.whzl.mengbi.chat.room.message.messagesActions;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;

import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.whzl.mengbi.chat.room.message.events.UpdatePubChatEvent;
import com.whzl.mengbi.chat.room.message.messageJson.GiftJson;
import com.whzl.mengbi.chat.room.message.messages.GiftMsg;
import com.whzl.mengbi.chat.room.util.ImageUrl;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.network.RequestManager;

import org.greenrobot.eventbus.EventBus;


public class GiftAction implements Actions {
    @Override
    public void performAction(String msgStr,  final Context context) {
        GiftJson giftJson = GsonUtils.GsonToBean(msgStr, GiftJson.class);
        if (giftJson == null) {
            return;
        }
        int giftPicId = giftJson.getContext().getGoodsPicId();
        final String giftUrl = ImageUrl.getImageUrl(giftPicId, "jpg", giftJson.getContext().getLastUpdateTime());

        SpannableString spanString = new SpannableString("gift");

        RequestManager.getInstance(context).getImage(giftUrl, new RequestManager.ReqCallBack<Object>(){
            @Override
            public void onReqSuccess(Object result) {
                Bitmap resource = (Bitmap) result;
                Drawable bitmapDrable = new BitmapDrawable(resource);
                int width = resource.getWidth();
                int height = resource.getHeight();
                bitmapDrable.setBounds(0, 0, DensityUtil.dp2px(width), DensityUtil.dp2px(height));
                ImageSpan imageSpan = new ImageSpan(bitmapDrable);
                spanString.setSpan(imageSpan, 0, spanString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                GiftMsg giftMsg = new GiftMsg(giftJson, context, spanString);
                UpdatePubChatEvent chatEvent = new UpdatePubChatEvent(giftMsg);
                EventBus.getDefault().post(chatEvent);
            }

            @Override
            public void onReqFailed(String errorMsg) {
                GiftMsg giftMsg = new GiftMsg(giftJson, context, null);
                UpdatePubChatEvent chatEvent = new UpdatePubChatEvent(giftMsg);
                EventBus.getDefault().post(chatEvent);
            }
        });
    }

}
