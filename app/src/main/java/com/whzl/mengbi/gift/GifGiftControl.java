package com.whzl.mengbi.gift;


import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;
import com.whzl.mengbi.chat.room.message.events.AnimEvent;
import com.whzl.mengbi.chat.room.message.messageJson.AnimJson;
import com.whzl.mengbi.util.glide.GlideImageLoader;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author shaw
 * @date 2018/8/3
 */
public class GifGiftControl {
    private Context mContext;
    //    private ImageView ivGif;
    private ArrayList<AnimEvent> mGifGiftQueue = new ArrayList<>();
    private boolean isShow;
    private SVGAImageView svgaImageView;

//    public GifGiftControl(Context context, ImageView imageView) {
//        mContext = context;
//        ivGif = imageView;
//    }

    public GifGiftControl(Context context, SVGAImageView svgaView) {
        mContext = context;
        svgaImageView = svgaView;
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
//            show(event);
            showSVGA(event);
            return;
        }
        mGifGiftQueue.add(event);
    }

//    private void show(AnimEvent event) {
//        isShow = true;
//        ivGif.setVisibility(View.VISIBLE);
//        GlideImageLoader.getInstace().loadGif(mContext, event.getAnimUrl(), ivGif, new GlideImageLoader.GifListener() {
//            @Override
//            public void onResourceReady() {
//                if (mContext == null) {
//                    return;
//                }
//                Observable.just(1)
//                        .delay(((long) (event.getSeconds() * 1000)), TimeUnit.MILLISECONDS)
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(integer -> {
//                            if (mContext == null) {
//                                return;
//                            }
//                            if (mGifGiftQueue.size() > 0) {
//                                show(mGifGiftQueue.get(0));
//                                mGifGiftQueue.remove(0);
//                            } else {
//                                isShow = false;
//                                ivGif.setVisibility(View.GONE);
//                            }
//                        });
//            }
//
//            @Override
//            public void onFail() {
//                if (mContext == null) {
//                    return;
//                }
//                Observable.just(1)
//                        .delay(((long) (event.getSeconds() * 1000)), TimeUnit.MILLISECONDS)
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .subscribe(integer -> {
//                            ivGif.setVisibility(View.GONE);
//                            if (mContext == null) {
//                                return;
//                            }
//                            if (mGifGiftQueue.size() > 0) {
//                                show(mGifGiftQueue.get(0));
//                                mGifGiftQueue.remove(0);
//                            } else {
//                                isShow = false;
//                            }
//                        });
//            }
//        });
//    }

    public void destroy() {
//        ivGif.setVisibility(View.GONE);
        svgaImageView.setVisibility(View.GONE);
        mGifGiftQueue.clear();
    }

    public void showSVGA(AnimEvent event) {
        svgaImageView.setVisibility(View.VISIBLE);
        svgaImageView.setLoops(1);
        SVGAParser parser = new SVGAParser(mContext);
        resetDownloader(parser);
        Log.i("SVGA", "event.getAnimUrl() = " + event.getAnimUrl());
        try {
            parser.parse(new URL(event.getAnimUrl()), new SVGAParser.ParseCompletion() {
                @Override
                public void onComplete(@NotNull SVGAVideoEntity videoItem) {
                    SVGADrawable drawable = new SVGADrawable(videoItem);
                    svgaImageView.setImageDrawable(drawable);
                    svgaImageView.startAnimation();
                }

                @Override
                public void onError() {

                }
            });
        } catch (Exception e) {
            System.out.print(true);
        }

        svgaImageView.setCallback(new SVGACallback() {
            @Override
            public void onPause() {
            }

            @Override
            public void onFinished() {
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
                                showSVGA(mGifGiftQueue.get(0));
                                mGifGiftQueue.remove(0);
                            }
                        });
            }

            @Override
            public void onRepeat() {
                stopAnim();
            }

            @Override
            public void onStep(int i, double v) {
            }
        });
    }

    /**
     * 设置下载器，这是一个可选的配置项。
     *
     * @param parser
     */
    private void resetDownloader(SVGAParser parser) {
        parser.setFileDownloader(new SVGAParser.FileDownloader() {
            @Override
            public void resume(final URL url, final Function1<? super InputStream, Unit> complete, final Function1<? super Exception, Unit> failure) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        OkHttpClient client = new OkHttpClient();
                        Request request = new Request.Builder().url(url).get().build();
                        try {
                            Response response = client.newCall(request).execute();
                            complete.invoke(response.body().byteStream());
                        } catch (IOException e) {
                            e.printStackTrace();
                            failure.invoke(e);
                        }
                    }
                }).start();
            }
        });
    }

    private void stopAnim(){
        if(svgaImageView.isAnimating() && mGifGiftQueue.size() == 0){
            svgaImageView.stopAnimation();
            svgaImageView.setVisibility(View.GONE);
        }
    }
}
