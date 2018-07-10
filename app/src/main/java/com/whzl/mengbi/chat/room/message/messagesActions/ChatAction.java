package com.whzl.mengbi.chat.room.message.messagesActions;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.whzl.mengbi.chat.room.message.events.UpdatePubChatEvent;
import com.whzl.mengbi.chat.room.message.messageJson.ChatCommonJson;
import com.whzl.mengbi.chat.room.message.messageJson.FromJson;
import com.whzl.mengbi.chat.room.message.messages.ChatMessage;
import com.whzl.mengbi.chat.room.util.ImageUrl;
import com.whzl.mengbi.util.GsonUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;


public class ChatAction implements Actions{
    private int finishedImageCount;
    @Override
    public void performAction(String msgStr, Context context) {
        ChatCommonJson chatJson = GsonUtils.GsonToBean(msgStr,ChatCommonJson.class);
        if(chatJson == null){
            return;
        }
        List<String> fromGoodsList = getGoodsUrlList(chatJson.getFrom_json());
        //List<String> toGoodsList = getGoodsUrlList(chatJson.getTo_json());
        if (fromGoodsList.isEmpty())  {
            ChatMessage chatMsg = new ChatMessage(chatJson, context, null);
            UpdatePubChatEvent chatEvent = new UpdatePubChatEvent(chatMsg);
            EventBus.getDefault().post(chatEvent);
            return;
        }
        SpannableString spanString = new SpannableString("0123456789");
        finishedImageCount = 0;
        for (String url:fromGoodsList) {
            Glide.with(context).asBitmap().load(url).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    ++finishedImageCount;
                    Drawable bitmapDrable = new BitmapDrawable(resource);
                    int width = resource.getWidth();
                    int height = resource.getHeight();
                    bitmapDrable.setBounds(0, 0, DensityUtil.dp2px(width), DensityUtil.dp2px(height));
                    ImageSpan imageSpan = new ImageSpan(bitmapDrable);
                    if (finishedImageCount >= fromGoodsList.size()) {
                        spanString.setSpan(imageSpan, finishedImageCount - 1, spanString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                        ChatMessage chatMsg = new ChatMessage(chatJson, context, spanString);
                        UpdatePubChatEvent chatEvent = new UpdatePubChatEvent(chatMsg);
                        EventBus.getDefault().post(chatEvent);
                    }else {
                        spanString.setSpan(imageSpan, finishedImageCount - 1, finishedImageCount, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    }
                }

                @Override
                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                    super.onLoadFailed(errorDrawable);
                    ++finishedImageCount;
                    if (finishedImageCount >= fromGoodsList.size()) {
                        ChatMessage chatMsg = new ChatMessage(chatJson, context, null);
                        UpdatePubChatEvent chatEvent = new UpdatePubChatEvent(chatMsg);
                        EventBus.getDefault().post(chatEvent);
                    }
                }
            });
        }
    }

    private List<String> getGoodsUrlList(FromJson fromJson) {
        if (fromJson == null) {
            return null;
        }
        List<FromJson.Good> goodsList = fromJson.getGoodsList();
        if (goodsList == null) {
            return null;
        }
        List<String> goodsUrlList = new ArrayList<>();
        for (FromJson.Good good :goodsList) {
            goodsUrlList.add(ImageUrl.getImageUrl(good.getGoodsIcon(), "jpp"));
        }
        return goodsUrlList;
    }
}
