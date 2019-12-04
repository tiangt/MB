package com.whzl.mengbi.gift;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.ImageView;

import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;
import com.whzl.mengbi.chat.room.message.events.AnimEvent;
import com.whzl.mengbi.chat.room.message.messageJson.AnimJson;
import com.whzl.mengbi.chat.room.util.ImageUrl;
import com.whzl.mengbi.model.entity.AnimResouseBean;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.LogUtils;
import com.whzl.mengbi.util.MediaPlayUtils;
import com.whzl.mengbi.util.glide.GlideImageLoader;

import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;

/**
 * @author nobody
 * @date 2019/5/9
 */
public class GifSvgaControl {
    private Context mContext;
    private ImageView ivGif;
    private boolean isShowGif;

    private ArrayList<AnimEvent> mGifGiftQueue = new ArrayList<>();
    private boolean isShowSvga;
    private SVGAImageView svgaImageView;
    private SVGAParser parser;
    private Disposable disposable;

    public GifSvgaControl(Context context, ImageView imageView, SVGAImageView svgaView) {
        mContext = context;
        ivGif = imageView;
        svgaImageView = svgaView;
        init();
    }

    public void loadAnim(AnimEvent event) {
        if (event == null || event.getAnimJson() == null || event.getAnimJson().getResources() == null) {
            return;
        }
        double seconds = 0;
        int count = 1;

        List<AnimJson.ResourcesEntity> resources = event.getAnimJson().getResources();
        for (int i = 0; i < resources.size(); i++) {
            AnimJson.ResourcesEntity resourcesEntity = resources.get(i);
            if ("PARAMS".equals(resourcesEntity.getResType())) {
                String resValue = resourcesEntity.getResValue();
                try {
                    AnimResouseBean animResouseBean = GsonUtils.GsonToBean(resValue, AnimResouseBean.class);
                    seconds = animResouseBean.playSeconds;
                    if (animResouseBean.loopCount == 0) {
                        count = 1;
                    } else {
                        count = animResouseBean.loopCount;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
//        if (seconds == 0) {
//            return;
//        }
        event.setSeconds(seconds);
        event.times = count;
        if (!isShowSvga && !isShowGif) {
            if ("MOBILE_GIFT_GIF".equals(event.getAnimJson().getAnimType())
                    || "MOBILE_CAR_GIF".equals(event.getAnimJson().getAnimType()))
                show(event);
            else if ("MOBILE_CAR_SVGA".equals(event.getAnimJson().getAnimType())
                    || "MOBILE_GIFT_SVGA".equals(event.getAnimJson().getAnimType())) {
                showSVGA(event);
            }
            return;
        }
        mGifGiftQueue.add(event);
    }


    private void init() {
        parser = new SVGAParser(mContext);
        svgaImageView.setCallback(new SVGACallback() {
            @Override
            public void onPause() {
                LogUtils.e("sssssssss    onPause");
//                destroy();
            }

            @Override
            public void onFinished() {
                LogUtils.e("sssssssss    onFinished");
                MediaPlayUtils.getInstance().stop();
                if (mContext == null) {
                    return;
                }
                if (mGifGiftQueue.size() > 0) {
                    if ("MOBILE_GIFT_GIF".equals(mGifGiftQueue.get(0).getAnimJson().getAnimType())
                            || "MOBILE_CAR_GIF".equals(mGifGiftQueue.get(0).getAnimJson().getAnimType())) {
                        show(mGifGiftQueue.get(0));
                        isShowSvga = false;
                        if (svgaImageView != null) {
                            svgaImageView.setVisibility(View.GONE);
                        }
                    } else if ("MOBILE_CAR_SVGA".equals(mGifGiftQueue.get(0).getAnimJson().getAnimType())
                            || "MOBILE_GIFT_SVGA".equals(mGifGiftQueue.get(0).getAnimJson().getAnimType())) {
                        showSVGA(mGifGiftQueue.get(0));
                        isShowGif = false;
                        ivGif.setVisibility(View.GONE);
                    }
                    mGifGiftQueue.remove(0);
                } else {
                    playEndReset();
                }
            }

            @Override
            public void onRepeat() {
                LogUtils.e("sssssssss    onRepeat");
//                stopAnim();
            }

            @Override
            public void onStep(int i, double v) {
            }
        });
    }

    public void showSVGA(AnimEvent event) {
        for (AnimJson.ResourcesEntity resource : event.getAnimJson().getResources()) {
            if (resource.getResType().equals("MP3")) {
                String mp3 = ImageUrl.getImageUrl(Integer.valueOf(resource.getResValue()), "mp3");
                int permission = 0;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    permission = mContext.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
                if (permission == PackageManager.PERMISSION_GRANTED) {
                    MediaPlayUtils.getInstance().play(mp3, resource.getResValue());
                } else {
                    MediaPlayUtils.getInstance().playByUrl(mp3);
                }
                break;
            }
        }
        isShowSvga = true;
        svgaImageView.setVisibility(View.VISIBLE);
        svgaImageView.setLoops(event.times);
        try {
            parser.decodeFromURL(new URL(event.getAnimUrl()), new SVGAParser.ParseCompletion() {
                @Override
                public void onComplete(@NotNull SVGAVideoEntity videoItem) {
                    svgaImageView.setVideoItem(videoItem);
                    svgaImageView.startAnimation();
                }

                @Override
                public void onError() {
                    isShowSvga = false;
                    svgaImageView.setVisibility(View.GONE);
                }
            });
        } catch (Exception e) {
            System.out.print(true);
        }
    }

    private void stopAnim() {
        if (svgaImageView.isAnimating() && mGifGiftQueue.size() == 0) {
            svgaImageView.stopAnimation();
            svgaImageView.setVisibility(View.GONE);
        }
    }


    private void show(AnimEvent event) {
        isShowGif = true;
        ivGif.setVisibility(View.VISIBLE);
        GlideImageLoader.getInstace().loadGif(mContext, event.getAnimUrl(), ivGif, new GlideImageLoader.GifListener() {
            @Override
            public void onResourceReady() {
                if (mContext == null) {
                    return;
                }
                disposable = Observable.just(1)
                        .delay(((long) (event.getSeconds() * 1000)), TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(integer -> {
                            if (mContext == null) {
                                return;
                            }
                            if (mGifGiftQueue.size() > 0) {
                                if ("MOBILE_GIFT_GIF".equals(mGifGiftQueue.get(0).getAnimJson().getAnimType())
                                        || "MOBILE_CAR_GIF".equals(mGifGiftQueue.get(0).getAnimJson().getAnimType())) {
                                    show(mGifGiftQueue.get(0));
                                    isShowSvga = false;
                                    if (svgaImageView != null) {
                                        svgaImageView.setVisibility(View.GONE);
                                    }
                                } else if ("MOBILE_CAR_SVGA".equals(mGifGiftQueue.get(0).getAnimJson().getAnimType())
                                        || "MOBILE_GIFT_SVGA".equals(mGifGiftQueue.get(0).getAnimJson().getAnimType())) {
                                    showSVGA(mGifGiftQueue.get(0));
                                    isShowGif = false;
                                    ivGif.setVisibility(View.GONE);
                                }
                                mGifGiftQueue.remove(0);
                            } else {
                                playEndReset();
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
                                if ("MOBILE_GIFT_GIF".equals(mGifGiftQueue.get(0).getAnimJson().getAnimType())
                                        || "MOBILE_CAR_GIF".equals(mGifGiftQueue.get(0).getAnimJson().getAnimType())) {
                                    show(mGifGiftQueue.get(0));
                                    isShowSvga = false;
                                    if (svgaImageView != null) {
                                        svgaImageView.setVisibility(View.GONE);
                                    }
                                } else if ("MOBILE_CAR_SVGA".equals(mGifGiftQueue.get(0).getAnimJson().getAnimType())
                                        || "MOBILE_GIFT_SVGA".equals(mGifGiftQueue.get(0).getAnimJson().getAnimType())) {
                                    showSVGA(mGifGiftQueue.get(0));
                                    isShowGif = false;
                                    ivGif.setVisibility(View.GONE);
                                }
                                mGifGiftQueue.remove(0);
                            } else {
                                playEndReset();
                            }
                        });
            }
        });
    }

    private void playEndReset() {
        isShowSvga = false;
        if (svgaImageView != null) {
            svgaImageView.setVisibility(View.GONE);
        }
        isShowGif = false;
        ivGif.setVisibility(View.GONE);
    }

    public void destroy() {
        if (ivGif != null) {
            ivGif.setVisibility(View.GONE);
        }
        if (svgaImageView != null) {
//            if (svgaImageView.isAnimating()) {
            svgaImageView.setCallback(null);
            svgaImageView.stopAnimation();
            svgaImageView.setVisibility(View.GONE);
//            }
        }
        mGifGiftQueue.clear();
        if (disposable != null) {
            disposable.dispose();
        }
        MediaPlayUtils.getInstance().destroy();
    }

}
