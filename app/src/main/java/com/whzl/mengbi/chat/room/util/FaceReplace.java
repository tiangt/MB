package com.whzl.mengbi.chat.room.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.whzl.mengbi.config.AppConfig;
import com.whzl.mengbi.model.entity.EmjoyInfo;
import com.whzl.mengbi.ui.common.BaseApplication;
import com.whzl.mengbi.util.AppUtils;
import com.whzl.mengbi.util.FileUtils;
import com.whzl.mengbi.util.GsonUtils;
import com.whzl.mengbi.util.UIUtil;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pl.droidsonroids.gif.GifDrawable;


public class FaceReplace {
    private List<FacePattern> patternList = null;
    private List<FacePattern> guardPatternList = null;
    private List<FacePattern> vipPatternList = null;
    private EmjoyInfo emjoyInfo = null;

    public void setGuardEmjoyInfo(EmjoyInfo guardEmjoyInfo) {
        this.guardEmjoyInfo = guardEmjoyInfo;
    }

    private EmjoyInfo guardEmjoyInfo = null;

    //add by chen on 2018/10/19
    private EmjoyInfo vipEmojiInfo = null;

    public void setVipEmojiInfo(EmjoyInfo vipEmojiInfo) {
        this.vipEmojiInfo = vipEmojiInfo;
    }

    public EmjoyInfo getVipEmojiInfo() {
        return vipEmojiInfo;
    }

    private static class FaceReplaceHolder {
        private static final FaceReplace instance = new FaceReplace();
    }

    public static final FaceReplace getInstance() {
        return FaceReplaceHolder.instance;
    }

    public void init(Context context) {
        if (patternList != null) {
            return;
        }
        String strJson = FileUtils.getJson("images/face/face.json", context);
        emjoyInfo = GsonUtils.GsonToBean(strJson, EmjoyInfo.class);
        List<EmjoyInfo.FaceBean.PublicBean> faceList = emjoyInfo.getFace().getPublicX();
        patternList = new ArrayList<>();
        for (EmjoyInfo.FaceBean.PublicBean faceBean : faceList) {
            byte[] fileContent = getFileContent(context, faceBean.getIcon());
            patternList.add(new FacePattern(Pattern.compile(faceBean.getValue()), fileContent, faceBean.getIcon(), faceBean.getValue()));
        }
        guardPatternList = new ArrayList<>();
        String guardStrJson = FileUtils.getJson("images/face/guard_face.json", context);
        guardEmjoyInfo = GsonUtils.GsonToBean(guardStrJson, EmjoyInfo.class);
        List<EmjoyInfo.FaceBean.PublicBean> guardFaceLost = guardEmjoyInfo.getFace().getPublicX();
        for (EmjoyInfo.FaceBean.PublicBean faceBean : guardFaceLost) {
            byte[] fileContent = getFileContent(context, faceBean.getIcon());
            guardPatternList.add(new FacePattern(Pattern.compile(faceBean.getValue()), fileContent, faceBean.getIcon(), faceBean.getValue()));
        }

        //VIP表情
        vipPatternList = new ArrayList<>();
        String vipStrJson = FileUtils.getJson("images/face/vip.json", context);
        vipEmojiInfo = GsonUtils.GsonToBean(vipStrJson, EmjoyInfo.class);
        List<EmjoyInfo.FaceBean.PublicBean> vipList = vipEmojiInfo.getFace().getPublicX();
        for (EmjoyInfo.FaceBean.PublicBean faceBean : vipList) {
            byte[] fileContent = getFileContent(context, faceBean.getIcon());
            vipPatternList.add(new FacePattern(Pattern.compile(faceBean.getValue()), fileContent, faceBean.getIcon(), faceBean.getValue()));
        }
    }

    public EmjoyInfo getGuardEmjoyInfo() {
        return guardEmjoyInfo;
    }

    public EmjoyInfo getEmjoyInfo() {
        return emjoyInfo;
    }

    public void faceReplace(TextView textView, SpannableString spanString, Context context) {
        textView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        DrawableCallback callback = new DrawableCallback(textView);
        for (FacePattern fp : patternList) {
            Matcher m = fp.getPattern().matcher(spanString);
            while (m.find()) {
                int start = m.start();
                int end = m.end();
                Drawable drawable = null;
                try {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT && BaseApplication.heapSize >= AppConfig.MAX_HEAP_SIZE) {
                        if ("/顶你".equals(fp.getValue()) || "/加油".equals(fp.getValue())) {
                            Bitmap bitmap = FileUtils.readBitmapFromAssetsFile(fp.getIcon(), context);
                            drawable = new BitmapDrawable(context.getResources(), bitmap);
                        } else {
                            drawable = new GifDrawable(fp.getFileContent());
                            drawable.setCallback(new DrawableCallback(textView));
                        }
                    } else {
                        Bitmap bitmap = FileUtils.readBitmapFromAssetsFile(fp.getIcon(), context);
                        drawable = new BitmapDrawable(context.getResources(), bitmap);
                    }
                } catch (Exception ignored) {
                    ignored.printStackTrace();
                }
                if (drawable == null) {
                    break;
                }
                drawable.setBounds(0, 0, UIUtil.sp2px(context, ImageUrl.IMAGE_HIGHT), UIUtil.sp2px(context, ImageUrl.IMAGE_HIGHT));
                ImageSpan span = new CenterAlignImageSpan(drawable);
                spanString.setSpan(span, start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            }
        }
    }

    public void guardFaceReplace(TextView textView, SpannableString spanString, Context context) {
        textView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        DrawableCallback callback = new DrawableCallback(textView);
        for (FacePattern fp : guardPatternList) {
            Matcher m = fp.getPattern().matcher(spanString);
            //Bitmap bitmap = FileUtils.readBitmapFromAssetsFile(facePath, context);
            while (m.find()) {
                int start = m.start();
                int end = m.end();
                Drawable drawable;
                try {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT&& BaseApplication.heapSize >= AppConfig.MAX_HEAP_SIZE) {
                        drawable = new GifDrawable(fp.getFileContent());
                        drawable.setCallback(new DrawableCallback(textView));
                    } else {
                        Bitmap bitmap = FileUtils.readBitmapFromAssetsFile(fp.getIcon(), context);
                        drawable = new BitmapDrawable(context.getResources(), bitmap);
                    }
                } catch (Exception ignored) {
                    break;
                }
                if (drawable == null) {
                    break;
                }
                drawable.setBounds(0, 0, DensityUtil.dp2px(31), DensityUtil.dp2px(31));
                ImageSpan span = new ImageSpan(drawable);
                spanString.setSpan(span, start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            }
        }
    }

    public void guardFaceReplace16(TextView textView, SpannableString spanString, Context context) {
        textView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        DrawableCallback callback = new DrawableCallback(textView);
        for (FacePattern fp : guardPatternList) {
            Matcher m = fp.getPattern().matcher(spanString);
            //Bitmap bitmap = FileUtils.readBitmapFromAssetsFile(facePath, context);
            while (m.find()) {
                int start = m.start();
                int end = m.end();
                Drawable drawable;
                try {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT&& BaseApplication.heapSize >= AppConfig.MAX_HEAP_SIZE) {
                        drawable = new GifDrawable(fp.getFileContent());
                        drawable.setCallback(new DrawableCallback(textView));
                    } else {
                        Bitmap bitmap = FileUtils.readBitmapFromAssetsFile(fp.getIcon(), context);
                        drawable = new BitmapDrawable(context.getResources(), bitmap);
                    }
                } catch (Exception ignored) {
                    break;
                }
                if (drawable == null) {
                    break;
                }
                drawable.setBounds(0, 0, DensityUtil.dp2px(20), DensityUtil.dp2px(20));
                ImageSpan span = new ImageSpan(drawable);
                spanString.setSpan(span, start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            }
        }
    }

    public void vipFaceReplace(TextView textView, SpannableString spanString, Context context) {
        textView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        DrawableCallback callback = new DrawableCallback(textView);
        for (FacePattern fp : vipPatternList) {
            Matcher m = fp.getPattern().matcher(spanString);
            while (m.find()) {
                int start = m.start();
                int end = m.end();
                Drawable drawable;
                try {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT&& BaseApplication.heapSize >= AppConfig.MAX_HEAP_SIZE) {
                        drawable = new GifDrawable(fp.getFileContent());
                        drawable.setCallback(new DrawableCallback(textView));
                    } else {
                        Bitmap bitmap = FileUtils.readBitmapFromAssetsFile(fp.getIcon(), context);
                        drawable = new BitmapDrawable(context.getResources(), bitmap);
                    }
                } catch (Exception e) {
                    break;
                }
                if (drawable == null) {
                    break;
                }
                drawable.setBounds(0, 0, DensityUtil.dp2px(50), DensityUtil.dp2px(50));
                ImageSpan span = new ImageSpan(drawable);
                spanString.setSpan(span, start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            }
        }
    }

    public void vipFaceReplace16(TextView textView, SpannableString spanString, Context context) {
        textView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        DrawableCallback callback = new DrawableCallback(textView);
        for (FacePattern fp : vipPatternList) {
            Matcher m = fp.getPattern().matcher(spanString);
            while (m.find()) {
                int start = m.start();
                int end = m.end();
                Drawable drawable;
                try {
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT&& BaseApplication.heapSize >= AppConfig.MAX_HEAP_SIZE) {
                        drawable = new GifDrawable(fp.getFileContent());
                        drawable.setCallback(new DrawableCallback(textView));
                    } else {
                        Bitmap bitmap = FileUtils.readBitmapFromAssetsFile(fp.getIcon(), context);
                        drawable = new BitmapDrawable(context.getResources(), bitmap);
                    }
                } catch (Exception e) {
                    break;
                }
                if (drawable == null) {
                    break;
                }
                drawable.setBounds(0, 0, DensityUtil.dp2px(20), DensityUtil.dp2px(20));
                ImageSpan span = new ImageSpan(drawable);
                spanString.setSpan(span, start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            }
        }
    }

    private byte[] getFileContent(Context context, String fileName) {
        try {
            InputStream in = context.getResources().getAssets().open(fileName);
            int length = in.available();
            byte[] buffer = new byte[length];

            in.read(buffer);
            in.close();
            return buffer;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private class FacePattern {
        private Pattern pattern;
        private byte[] fileContent;
        private String icon;
        private String value;

        public FacePattern(Pattern pattern, byte[] fileContent, String icon, String value) {
            this.pattern = pattern;
            this.fileContent = fileContent;
            this.icon = icon;
            this.value = value;
        }

        public Pattern getPattern() {
            return pattern;
        }

        public void setPattern(Pattern pattern) {
            this.pattern = pattern;
        }

        public byte[] getFileContent() {
            return fileContent;
        }

        public void setFileContent(byte[] fileContent) {
            this.fileContent = fileContent;
        }

        public String getIcon() {
            return icon;
        }

        public String getValue() {
            return value;
        }
    }
}
