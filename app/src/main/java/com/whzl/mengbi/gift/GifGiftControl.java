package com.whzl.mengbi.gift;


import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.whzl.mengbi.chat.room.message.events.AnimEvent;
import com.whzl.mengbi.chat.room.message.messageJson.AnimJson;
import com.whzl.mengbi.util.glide.GlideImageLoader;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * @author shaw
 * @date 2018/8/3
 */
public class GifGiftControl {
    private Context mContext;
    private ImageView ivGif;
    private ArrayList<AnimEvent> mGifGiftQueue = new ArrayList<>();
    private boolean isShow;

    public GifGiftControl(Context context, ImageView imageView) {
        mContext = context;
        ivGif = imageView;
    }

    public void load(AnimEvent event) {
        if (event == null || event.getAnimJson() == null || event.getAnimJson().getResources() == null) {
            return;
        }
        double seconds = 0;
        List<AnimJson.ResourcesEntity> resources = event.getAnimJson().getResources();
        for (int i = 0; i < resources.size(); i++) {
            AnimJson.ResourcesEntity resourcesEntity = resources.get(i);
            if ("PARAMS".equals(resourcesEntity.getResType())) {
                String resValue = resourcesEntity.getResValue();
                try {
                    JSONObject jsonObject = new JSONObject(resValue);
                    seconds = jsonObject.getDouble("playSeconds");
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
        if (seconds == 0) {
            return;
        }
        event.setSeconds(seconds);
        if (!isShow) {
            show(event);
            return;
        }
        mGifGiftQueue.add(event);
    }

    private void show(AnimEvent event) {
        isShow = true;
        ivGif.setVisibility(View.VISIBLE);
        GlideImageLoader.getInstace().loadGif(mContext, event.getAnimUrl(), ivGif, new GlideImageLoader.GifListener() {
            @Override
            public void onResourceReady() {
                if (mContext == null) {
                    return;
                }
                Observable.just(1)
                        .delay(((long) (event.getSeconds() * 1000)), TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(integer -> {
                            if (mContext == null) {
                                return;
                            }
                            if (mGifGiftQueue.size() > 0) {
                                show(mGifGiftQueue.get(0));
                                mGifGiftQueue.remove(0);
                            } else {
                                isShow = false;
                                ivGif.setVisibility(View.GONE);
                            }
                        });
            }

            @Override
            public void onFail() {
                if (mContext == null) {
                    return;
                }
                Observable.just(1)
                        .delay(((long) (event.getSeconds() * 1000)), TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(integer -> {
                            ivGif.setVisibility(View.GONE);
                            if (mContext == null) {
                                return;
                            }
                            if (mGifGiftQueue.size() > 0) {
                                show(mGifGiftQueue.get(0));
                                mGifGiftQueue.remove(0);
                            } else {
                                isShow = false;
                            }
                        });
            }
        });
    }

    public void destroy() {
        mContext = null;
        mGifGiftQueue = null;
    }
}