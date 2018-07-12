package com.whzl.mengbi.chat.room.message.messagesActions;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;

import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.whzl.mengbi.chat.room.message.events.UpdatePubChatEvent;
import com.whzl.mengbi.chat.room.message.messageJson.WelcomeJson;
import com.whzl.mengbi.chat.room.message.messages.WelcomeMsg;
import com.whzl.mengbi.chat.room.util.CenterAlignImageSpan;
import com.whzl.mengbi.chat.room.util.ImageUrl;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.network.RequestManager;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;


public class WelComeAction implements Actions{
    private int finishedImageCount = 0;
    private final static ReentrantLock lock = new ReentrantLock();
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
        List<SpannableString> spanList = new ArrayList<>();
        HashMap<String, Integer> imageIndexMap = new HashMap<String, Integer>();
        for(int i = 0; i < goodsUrlList.size(); ++i) {
            imageIndexMap.put(goodsUrlList.get(i), i);
            spanList.add(new SpannableString(Integer.toString(i)));
        }
        for (String url:goodsUrlList) {
            String imageUrl = url;
            RequestManager.getInstance(context).getImage(imageUrl, new RequestManager.ReqCallBack<Object>() {
                @Override
                public void onReqSuccess(Object result) {
                    addFinishedCount();
                    Bitmap resource = (Bitmap)result;
                    Drawable bitmapDrable = new BitmapDrawable(resource);
                    int width = resource.getWidth();
                    int height = resource.getHeight();
                    float dpWidth = width * ImageUrl.IMAGE_HIGHT / height;
                    bitmapDrable.setBounds(0, 0, DensityUtil.dp2px(dpWidth), DensityUtil.dp2px(ImageUrl.IMAGE_HIGHT));
                    CenterAlignImageSpan imageSpan = new CenterAlignImageSpan(bitmapDrable);
                    SpannableString spanString = new SpannableString(imageUrl);
                    spanString.setSpan(imageSpan, 0, spanString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    int index = imageIndexMap.get(imageUrl);
                    spanList.set(index, spanString);
                    if (finishedImageCount >= goodsUrlList.size()) {
                        WelcomeMsg welcomeMsg = new WelcomeMsg(welcomeJson, context, spanList);
                        UpdatePubChatEvent chatEvent = new UpdatePubChatEvent(welcomeMsg);
                        EventBus.getDefault().post(chatEvent);
                    }
                }

                @Override
                public void onReqFailed(String errorMsg) {
                    addFinishedCount();
                    LogUtils.e("onReqFailed"+errorMsg);
                    if (finishedImageCount >= goodsUrlList.size()) {
                        WelcomeMsg welcomeMsg = new WelcomeMsg(welcomeJson, context, spanList);
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

    private void addFinishedCount() {
        lock.lock();
        ++finishedImageCount;
        lock.unlock();
    }
}
