package com.whzl.mengbi.chat.room.message.messagesActions;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;

import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.whzl.mengbi.chat.room.message.events.RunWayEvent;
import com.whzl.mengbi.chat.room.message.messageJson.RunWayJson;
import com.whzl.mengbi.chat.room.util.CenterAlignImageSpan;
import com.whzl.mengbi.chat.room.util.ImageUrl;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.network.RequestManager;

import org.greenrobot.eventbus.EventBus;

public class RunWayAction implements Actions {
    @Override
    public void performAction(String msgStr, Context context) {
        RunWayJson runWayJson = GsonUtils.GsonToBean(msgStr, RunWayJson.class);
        if (null == runWayJson) {
            return;
        }
        int goodsPicId = runWayJson.getContext().getGoodsPicId();
        String goodsPicUrl = ImageUrl.getImageUrl(goodsPicId, "jpg");
        RequestManager.getInstance(context).getImage(goodsPicUrl, new RequestManager.ReqCallBack<Object>() {
            @Override
            public void onReqSuccess(Object result) {
                Bitmap resource = (Bitmap)result;
                Drawable bitmapDrable = new BitmapDrawable(resource);
                int width = resource.getWidth();
                int height = resource.getHeight();
                float dpWidth = width * ImageUrl.IMAGE_HIGHT / height;
                bitmapDrable.setBounds(0, 0, DensityUtil.dp2px(dpWidth), DensityUtil.dp2px(ImageUrl.IMAGE_HIGHT));
                CenterAlignImageSpan imageSpan = new CenterAlignImageSpan(bitmapDrable);
                SpannableString spanString = new SpannableString(goodsPicUrl);
                spanString.setSpan(imageSpan, 0, spanString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                EventBus.getDefault().post(new RunWayEvent(context, runWayJson, spanString));
            }

            @Override
            public void onReqFailed(String errorMsg) {
                LogUtils.e("onReqFailed"+errorMsg);
                EventBus.getDefault().post(new RunWayEvent(context, runWayJson, null));
            }
        });
    }
}
