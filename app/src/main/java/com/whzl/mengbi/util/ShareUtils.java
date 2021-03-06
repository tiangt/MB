package com.whzl.mengbi.util;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author cliang
 * @date 2018.11.28
 */
public class ShareUtils {

    /**
     * 分享链接
     *
     * @param activity
     * @param webUrl      直播间的h5链接地址
     * @param imageUrl    主播封面
     * @param title       标题：我正在偷偷观看 主播昵称 的直播！
     * @param description 内容：主播昵称 正在性感热舞中，快去瞧瞧吧！
     * @param platform
     */
    public static void shareWeb(Activity activity, String webUrl, String imageUrl,
                                String title, String description, SHARE_MEDIA platform) {
        UMWeb web = new UMWeb(webUrl);
        web.setTitle(title);
        web.setDescription(description);
        web.setThumb(new UMImage(activity, imageUrl));
        new ShareAction(activity)
                .setPlatform(platform)
                .withMedia(web)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onResult(final SHARE_MEDIA share_media) {
                        activity.runOnUiThread(() -> {
                            if (share_media == SHARE_MEDIA.WEIXIN || share_media == SHARE_MEDIA.WEIXIN_CIRCLE) {
                                return;
                            }
                            Toast.makeText(activity, "分享成功", Toast.LENGTH_SHORT).show();
                        });
                    }

                    @Override
                    public void onError(final SHARE_MEDIA share_media, final Throwable throwable) {
                        if (throwable != null) {
                            Toast.makeText(activity, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancel(final SHARE_MEDIA share_media) {
                        activity.runOnUiThread(() -> {
                            Toast.makeText(activity, "分享取消", Toast.LENGTH_SHORT).show();
                        });
                    }
                }).share();
    }


    /**
     * 分享图片
     */
    public static void shareBitmap(final Activity activity, Bitmap bitmap, SHARE_MEDIA platform) {
        UMImage image = new UMImage(activity, bitmap);//bitmap文件
        image.compressStyle = UMImage.CompressStyle.SCALE;//大小压缩，默认为大小压缩，适合普通很大的图
//        image.compressStyle = UMImage.CompressStyle.QUALITY;//质量压缩，适合长图的分享
//        image.compressFormat = Bitmap.CompressFormat.PNG;//用户分享透明背景的图片可以设置这种方式，但是qq好友，微信朋友圈，不支持透明背景图片，会变成黑色

        new ShareAction(activity)
                .setPlatform(platform)
                .withMedia(image)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onResult(final SHARE_MEDIA share_media) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (share_media == SHARE_MEDIA.WEIXIN || share_media == SHARE_MEDIA.WEIXIN_CIRCLE) {
                                    return;
                                }
                                Toast.makeText(activity, "分享成功", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onError(final SHARE_MEDIA share_media, final Throwable throwable) {
                        if (throwable != null) {
                            Log.d("throw", "throw:" + throwable.getMessage());
                        }
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity, "分享失败", Toast.LENGTH_SHORT).show();

                            }
                        });
                    }

                    @Override
                    public void onCancel(final SHARE_MEDIA share_media) {
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(activity, "分享取消", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                })
                .share();

        //新浪微博中图文+链接
        /*new ShareAction(activity)
                .setPlatform(platform)
                .withText(description + " " + WebUrl)
                .withMedia(new UMImage(activity,imageID))
                .share();*/
    }


}
