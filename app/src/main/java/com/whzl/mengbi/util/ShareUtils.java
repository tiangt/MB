package com.whzl.mengbi.util;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

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
//        UMShareAPI mShareAPI = UMShareAPI.get(activity);
//        if (mShareAPI.isInstall(activity, platform)) {
//            Toast.makeText(activity, "没有安装客户端", Toast.LENGTH_SHORT).show();
//            return;
//        }
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


}
