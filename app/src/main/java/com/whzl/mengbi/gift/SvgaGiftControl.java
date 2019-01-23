package com.whzl.mengbi.gift;

import android.content.Context;
import android.view.View;

import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGADrawable;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;
import com.whzl.mengbi.chat.room.message.events.AnimEvent;
import com.whzl.mengbi.chat.room.message.messageJson.AnimJson;
import com.whzl.mengbi.util.LogUtils;

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
import io.reactivex.disposables.Disposable;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @author cliang
 * @date 2019.1.3
 */
public class SvgaGiftControl {
    private Context mContext;
    private ArrayList<AnimEvent> mGifGiftQueue = new ArrayList<>();
    private boolean isShow;
    private SVGAImageView svgaImageView;
    private Disposable disposable;

    public SvgaGiftControl(Context context, SVGAImageView svgaView) {
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
            showSVGA(event);
            return;
        }
        mGifGiftQueue.add(event);
    }

    public void destroy() {
        if (disposable != null) {
            disposable.dispose();
        }
        if (svgaImageView != null) {
            svgaImageView.setVisibility(View.GONE);
        }
        mGifGiftQueue.clear();
    }

    public void showSVGA(AnimEvent event) {
        isShow = true;
        svgaImageView.setVisibility(View.VISIBLE);
        svgaImageView.setLoops(1);
        SVGAParser parser = new SVGAParser(mContext);
        resetDownloader(parser);
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
                disposable = Observable.just(1)
                        .delay(((long) (event.getSeconds() * 1000)), TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(integer -> {
                            if (mContext == null) {
                                return;
                            }
                            if (mGifGiftQueue.size() > 0) {
                                showSVGA(mGifGiftQueue.get(0));
                                mGifGiftQueue.remove(0);
                            } else {
                                isShow = false;
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
                        LogUtils.e("Thread.currentThread().getId() " + Thread.currentThread().getId());
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

    private void stopAnim() {
        if (svgaImageView.isAnimating() && mGifGiftQueue.size() == 0) {
            svgaImageView.stopAnimation();
            svgaImageView.setVisibility(View.GONE);
        }
    }
}
