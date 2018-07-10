package com.whzl.mengbi.chat.room.message.messagesActions;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.whzl.mengbi.chat.room.message.events.UpdatePubChatEvent;
import com.whzl.mengbi.chat.room.message.messageJson.WelcomeJson;
import com.whzl.mengbi.chat.room.message.messages.WelcomeMsg;
import com.whzl.mengbi.chat.room.util.ImageUrl;
import com.whzl.mengbi.util.GsonUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;


public class WelComeAction implements Actions{
    private int finishedImageCount;
    @Override
    public void performAction(String msgStr, final Context context) {
        WelcomeJson welcomeJson = GsonUtils.GsonToBean(msgStr, WelcomeJson.class);
        if (null == welcomeJson) {
            return;
        }
        List<String> goodsUrlList = getGoodsList(welcomeJson.getContext().getInfo().getUserBagList());
        if (goodsUrlList.isEmpty()) {
            WelcomeMsg welMsg = new WelcomeMsg(welcomeJson, context, null);
            EventBus.getDefault().post(new UpdatePubChatEvent(welMsg));
            return;
        }
        finishedImageCount = 0;
        SpannableString spanString = new SpannableString("0123456789");
        for (String url:goodsUrlList) {
            Glide.with(context).asBitmap().load(url).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    ++finishedImageCount;
                    Drawable bitmapDrable = new BitmapDrawable(resource);
                    int width = resource.getWidth();
                    int height = resource.getHeight();
                    bitmapDrable.setBounds(0, 0, DensityUtil.dp2px(width), DensityUtil.dp2px(height));
                    ImageSpan imageSpan = new ImageSpan(bitmapDrable);
                    if (finishedImageCount >= goodsUrlList.size()) {
                        spanString.setSpan(imageSpan, finishedImageCount - 1, spanString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                        WelcomeMsg welcomeMsg = new WelcomeMsg(welcomeJson, context, spanString);
                        UpdatePubChatEvent chatEvent = new UpdatePubChatEvent(welcomeMsg);
                        EventBus.getDefault().post(chatEvent);
                    } else {
                        spanString.setSpan(imageSpan, finishedImageCount - 1, finishedImageCount, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    }
                }

                @Override
                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                    super.onLoadFailed(errorDrawable);
                    ++finishedImageCount;
                    if (finishedImageCount >= goodsUrlList.size()) {
                        WelcomeMsg welcomeMsg = new WelcomeMsg(welcomeJson, context, null);
                        UpdatePubChatEvent chatEvent = new UpdatePubChatEvent(welcomeMsg);
                        EventBus.getDefault().post(chatEvent);
                    }
                }
            });
        }
    }

    private boolean getIsGuizu(List<WelcomeJson.WelcomeLevelListItem> levelList){
        for(WelcomeJson.WelcomeLevelListItem item : levelList){
            if(item.getLevelType().equals("ROYAL_LEVEL")){
                if(item.getLevelValue() > 0){
                    return true;
                }
            }
        }
        return false;
    }

    private int getGuizuLevel(List<WelcomeJson.WelcomeLevelListItem> levelList){
        for(WelcomeJson.WelcomeLevelListItem item : levelList){
            if(item.getLevelType().equals("ROYAL_LEVEL")){
                return item.getLevelValue();
            }
        }
        return 1;
    }

    private int getUserLevel(List<WelcomeJson.WelcomeLevelListItem> levelList){
        if(levelList == null){
            return 1;
        }
        for(WelcomeJson.WelcomeLevelListItem item : levelList){
            if(!TextUtils.isEmpty(item.getLevelType())&&item.getLevelType().equals("USER_LEVEL")){
                return item.getLevelValue();
            }
        }
        return 1;
    }

    private boolean hasBagCar(List<WelcomeJson.UserBagItem> carList){
        for(WelcomeJson.UserBagItem bagItem : carList){
            if(bagItem.getGoodsType().equals("CAR") && bagItem.getIsEquip().equals("T")){
                return true;
            }
        }
        return false;
    }


    private List<String> getGoodsList(List<WelcomeJson.UserBagItem> bagList) {
        List<String> goodsUrlList = new ArrayList<>();
        if (null == bagList) {
            return goodsUrlList;
        }
        for(WelcomeJson.UserBagItem item : bagList) {
            if (item.getGoodsType().equals("BADGE") && item.getIsEquip().equals("T")) {
                int goodId = item.getGoodsPicId();
                goodsUrlList.add(ImageUrl.getImageUrl(goodId, "jpg"));
            }
        }
        return goodsUrlList;
    }
}
