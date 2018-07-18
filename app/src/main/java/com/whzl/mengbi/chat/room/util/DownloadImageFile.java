package com.whzl.mengbi.chat.room.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;

import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.network.RequestManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * author: yaobo wu
 * date:   On 2018/7/18
 */
public class DownloadImageFile {
    private DownloadEvent downloadEvent;
    private AtomicInteger finishedImageCount = new AtomicInteger(0);

    public DownloadImageFile(DownloadEvent downloadEvent) {
        this.downloadEvent = downloadEvent;
    }

    public void doDownload(List<String> imageUrlList, Context context) {
        List<SpannableString> spanList = new ArrayList<>();
        HashMap<String, Integer> imageIndexMap = new HashMap<String, Integer>();
        for(int i = 0; i < imageUrlList.size(); ++i) {
            imageIndexMap.put(imageUrlList.get(i), i);
            spanList.add(new SpannableString(Integer.toString(i)));
        }
        for (String url:imageUrlList) {
            String imageUrl = url;
            RequestManager.getInstance(context).getImage(imageUrl, new RequestManager.ReqCallBack<Object>() {
                @Override
                public void onReqSuccess(Object result) {
                    finishedImageCount.incrementAndGet();
                    Bitmap resource = (Bitmap) result;
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
                    if (finishedImageCount.get() >= imageUrlList.size()) {
                        downloadEvent.finished(spanList);
                    }
                }

                @Override
                public void onReqFailed(String errorMsg) {
                    finishedImageCount.incrementAndGet();
                    LogUtils.e("onReqFailed" + errorMsg);
                    if (finishedImageCount.get() >= imageUrlList.size()) {
                        downloadEvent.finished(null);
                    }
                }
            });
        }
    }

}
