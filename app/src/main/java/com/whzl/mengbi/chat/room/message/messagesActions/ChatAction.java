package com.whzl.mengbi.chat.room.message.messagesActions;

import android.content.Context;
import android.text.SpannableString;

import com.whzl.mengbi.chat.room.message.events.UpdatePubChatEvent;
import com.whzl.mengbi.chat.room.message.messageJson.ChatCommonJson;
import com.whzl.mengbi.chat.room.message.messageJson.FromJson;
import com.whzl.mengbi.chat.room.message.messages.ChatMessage;
import com.whzl.mengbi.chat.room.util.DownloadEvent;
import com.whzl.mengbi.chat.room.util.DownloadImageFile;
import com.whzl.mengbi.chat.room.util.ImageUrl;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;


public class ChatAction implements Actions {
    @Override
    public void performAction(String msgStr, Context context) {
        LogUtils.e2(msgStr);
        ChatCommonJson chatJson = GsonUtils.GsonToBean(msgStr, ChatCommonJson.class);
        if (chatJson == null) {
            return;
        }
        List<String> fromGoodsList = getGoodsUrlList(chatJson.getFrom_json());
        if (fromGoodsList == null || fromGoodsList.isEmpty()) {
            ChatMessage chatMsg = new ChatMessage(chatJson, context, null, false);
            UpdatePubChatEvent chatEvent = new UpdatePubChatEvent(chatMsg);
            EventBus.getDefault().post(chatEvent);
            return;
        }
        DownloadImageFile downloadImageFile = new DownloadImageFile(new DownloadEvent() {
            @Override
            public void finished(List<SpannableString> imageSpanList) {
                ChatMessage chatMsg = new ChatMessage(chatJson, context, imageSpanList, false);
                UpdatePubChatEvent chatEvent = new UpdatePubChatEvent(chatMsg);
                EventBus.getDefault().post(chatEvent);
            }
        });
        downloadImageFile.doDownload(fromGoodsList, context);
//        List<SpannableString> spanList = new ArrayList<>();
//        HashMap<String, Integer> imageIndexMap = new HashMap<String, Integer>();
//        for(int i = 0; i < fromGoodsList.size(); ++i) {
//            imageIndexMap.put(fromGoodsList.get(i), i);
//            spanList.add(new SpannableString(Integer.toString(i)));
//        }
//        for (String url:fromGoodsList) {
//            String imageUrl = url;
//            RequestManager.getInstance(context).getImage(imageUrl, new RequestManager.ReqCallBack<Object>() {
//                @Override
//                public void onReqSuccess(Object result) {
//                    addFinishedCount();
//                    Bitmap resource = (Bitmap)result;
//                    Drawable bitmapDrable = new BitmapDrawable(resource);
//                    int width = resource.getWidth();
//                    int height = resource.getHeight();
//                    float dpWidth = width * ImageUrl.IMAGE_HIGHT / height;
//                    bitmapDrable.setBounds(0, 0, DensityUtil.dp2px(dpWidth), DensityUtil.dp2px(ImageUrl.IMAGE_HIGHT));
//                    CenterAlignImageSpan imageSpan = new CenterAlignImageSpan(bitmapDrable);
//                    SpannableString spanString = new SpannableString(imageUrl);
//                    spanString.setSpan(imageSpan, 0, spanString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//                    int index = imageIndexMap.get(imageUrl);
//                    spanList.set(index, spanString);
//                    if (finishedImageCount.get() >= fromGoodsList.size()) {
//                        Log.e("ChatAction", "finishedCount=" + finishedImageCount.get() + " post msg");
//                        ChatMessage chatMsg = new ChatMessage(chatJson, context, spanList);
//                        UpdatePubChatEvent chatEvent = new UpdatePubChatEvent(chatMsg);
//                        EventBus.getDefault().post(chatEvent);
//                    }
//                }
//
//                @Override
//                public void onReqFailed(String errorMsg) {
//                    addFinishedCount();
//                    LogUtils.e("onReqFailed"+errorMsg);
//                    if (finishedImageCount.get() >= fromGoodsList.size()) {
//                        ChatMessage chatMsg = new ChatMessage(chatJson, context, null);
//                        UpdatePubChatEvent chatEvent = new UpdatePubChatEvent(chatMsg);
//                        EventBus.getDefault().post(chatEvent);
//                    }
//                }
//            });
//        }
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
        for (FromJson.Good good : goodsList) {
            if (good.getGoodsType().equals("BADGE")) {
                goodsUrlList.add(ImageUrl.getImageUrl(good.getGoodsIcon(), "jpg"));
            }
        }
        return goodsUrlList;
    }

}
