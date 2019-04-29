package com.whzl.mengbi.gift;

import android.content.Context;
import android.view.View;

import com.opensource.svgaplayer.SVGACallback;
import com.opensource.svgaplayer.SVGAImageView;
import com.opensource.svgaplayer.SVGAParser;
import com.opensource.svgaplayer.SVGAVideoEntity;
import com.whzl.mengbi.chat.room.message.events.AnimEvent;
import com.whzl.mengbi.chat.room.message.messageJson.AnimJson;
import com.whzl.mengbi.util.LogUtils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * @author cliang
 * @date 2019.1.3
 */
public class SvgaGiftControl {
    private Context mContext;
    private ArrayList<AnimEvent> mGifGiftQueue = new ArrayList<>();
    private boolean isShow;
    private SVGAImageView svgaImageView;
    private SVGAParser parser;

    public SvgaGiftControl(Context context, SVGAImageView svgaView) {
        mContext = context;
        svgaImageView = svgaView;
        init();
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
        if (svgaImageView != null) {
            if (svgaImageView.isAnimating()) {
                svgaImageView.stopAnimation();
                svgaImageView.setVisibility(View.GONE);
            }
        }
        mGifGiftQueue.clear();
    }

    private void init() {
        svgaImageView.setLoops(1);
        parser = new SVGAParser(mContext);

        svgaImageView.setCallback(new SVGACallback() {
            @Override
            public void onPause() {
                LogUtils.e("sssssssss    onPause");
                destroy();
            }

            @Override
            public void onFinished() {
                LogUtils.e("sssssssss    onFinished");
                if (mContext == null) {
                    return;
                }
                if (mGifGiftQueue.size() > 0) {
                    showSVGA(mGifGiftQueue.get(0));
                    mGifGiftQueue.remove(0);
                } else {
                    isShow = false;
                    if (svgaImageView != null) {
                        svgaImageView.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onRepeat() {
                LogUtils.e("sssssssss    onRepeat");
                stopAnim();
            }

            @Override
            public void onStep(int i, double v) {
            }
        });
    }

    public void showSVGA(AnimEvent event) {
        isShow = true;
        svgaImageView.setVisibility(View.VISIBLE);
        try {
            parser.decodeFromURL(new URL(event.getAnimUrl()), new SVGAParser.ParseCompletion() {
                @Override
                public void onComplete(@NotNull SVGAVideoEntity videoItem) {
                    svgaImageView.setVideoItem(videoItem);
                    svgaImageView.startAnimation();
                }

                @Override
                public void onError() {

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
}
