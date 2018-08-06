package com.whzl.mengbi.chat.room.util;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.view.View;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.whzl.mengbi.model.entity.EmjoyInfo;
import com.whzl.mengbi.util.FileUtils;
import com.whzl.mengbi.util.GsonUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import pl.droidsonroids.gif.GifDrawable;


public class FaceReplace {
    private List<FacePattern> patternList = null;
    private EmjoyInfo emjoyInfo = null;

    public void setGuardEmjoyInfo(EmjoyInfo guardEmjoyInfo) {
        this.guardEmjoyInfo = guardEmjoyInfo;
    }

    private EmjoyInfo guardEmjoyInfo = null;
    private static class FaceReplaceHolder {
        private static final FaceReplace instance = new FaceReplace();
    }

    public static final FaceReplace getInstance(){
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
        for(EmjoyInfo.FaceBean.PublicBean faceBean: faceList) {
            byte[] fileContent = getFileContent(context, faceBean.getIcon());
            patternList.add(new FacePattern(Pattern.compile(faceBean.getValue()), fileContent));
        }
        String guardStrJson = FileUtils.getJson("images/face/guard_face.json", context);
        guardEmjoyInfo = GsonUtils.GsonToBean(guardStrJson, EmjoyInfo.class);

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
        for(FacePattern fp : patternList) {
            Matcher m = fp.getPattern().matcher(spanString);
            //Bitmap bitmap = FileUtils.readBitmapFromAssetsFile(facePath, context);
            while(m.find()) {
                int start = m.start();
                int end = m.end();
                Drawable drawable;
                try {
                    drawable = new GifDrawable(fp.getFileContent());
                    drawable.setCallback(new DrawableCallback(textView));
                } catch (Exception ignored) {
                    break;
                }
                if (drawable == null) {
                    break;
                }
                drawable.setBounds(0, 0, DensityUtil.dp2px(ImageUrl.IMAGE_HIGHT), DensityUtil.dp2px(ImageUrl.IMAGE_HIGHT));
                ImageSpan span = new CenterAlignImageSpan(drawable);
                spanString.setSpan(span, start, end, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            }
        }
    }

    private byte[] getFileContent(Context context, String fileName) {
        try{
            InputStream in = context.getResources().getAssets().open(fileName);
            int length = in.available();
            byte [] buffer = new byte[length];

            in.read(buffer);
            in.close();
            return buffer;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private class FacePattern {
        private Pattern pattern;
        private byte[] fileContent;

        public FacePattern(Pattern pattern, byte[] fileContent) {
            this.pattern = pattern;
            this.fileContent = fileContent;
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
    }
}
