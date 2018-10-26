package com.whzl.mengbi.chat.room.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.whzl.mengbi.chat.room.message.messageJson.FromJson;

import java.io.IOException;
import java.util.List;

import pl.droidsonroids.gif.GifDrawable;


public class LevelUtil {
    public static int getUserLevel(FromJson fromJson){
        if(fromJson == null){
            return -1;
        }
        List<FromJson.Level> levelList = fromJson.getLevelList();
        if (levelList == null) {
            return -1;
        }
        for(FromJson.Level levelItem : levelList){
            if(levelItem.getLevelType().equals("USER_LEVEL")){
                int levelValue = levelItem.getLevelValue();
                return levelValue;
            }
        }
        return -1;
    }

    public static int getAnchorLevel(FromJson fromJson) {
        if (fromJson == null) {
            return -1;
        }
        List<FromJson.Level> levelList = fromJson.getLevelList();
        if (levelList == null) {
            return -1;
        }
        for(FromJson.Level levelItem: levelList) {
            if (levelItem.getLevelType().equals("ANCHOR_LEVEL")) {
                int levelVal = levelItem.getLevelValue();
                return levelVal;
            }
        }
        return -1;
    }

    public static int getRoyalLevel(FromJson fromJson){
        if(fromJson == null){
            return -1;
        }
        List<FromJson.Level> levelList = fromJson.getLevelList();
        if (levelList == null) {
            return -1;
        }
        for(FromJson.Level levelItem : levelList){
            if(levelItem.getLevelType().equals("ROYAL_LEVEL")){
                int levelValue = levelItem.getLevelValue();
                return levelValue;
            }
        }
        return -1;
    }


    /**
     * 根据图片资源获取spanString
     * @param context
     * @param resourceId 图片资源
     * @return
     */
    public static SpannableString getImageResourceSpan(Context context, int resourceId) {
        SpannableString levelIcon = new SpannableString("icon");
        Resources res = context.getResources();
        Drawable levelIconDrawable = res.getDrawable(resourceId);
        if(levelIconDrawable == null){
            return levelIcon;
        }
        int originWidth = levelIconDrawable.getIntrinsicWidth();
        int originHeight = levelIconDrawable.getIntrinsicHeight();
        float dpHeight = ImageUrl.IMAGE_HIGHT;
        float dpWidth = originWidth * dpHeight / originHeight;
        levelIconDrawable.setBounds(0, 0, DensityUtil.dp2px(dpWidth), DensityUtil.dp2px(dpHeight));
        CenterAlignImageSpan imageSpan = new CenterAlignImageSpan(levelIconDrawable);
        levelIcon.setSpan(imageSpan,0,levelIcon.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return levelIcon;
    }


    /**
     * 根据图片资源获取spanString
     * @param context
     * @param resourceId 图片资源
     * @return
     */
    public static SpannableString getRoyalImageResourceSpan(Context context, int resourceId,TextView textView) throws IOException {
        SpannableString levelIcon = new SpannableString("icon");
        Drawable drawable = null;
        drawable = new GifDrawable(context.getResources(),resourceId);
        drawable.setCallback(new DrawableCallback(textView));
        if (drawable != null) {
            drawable.setBounds(0, 0, DensityUtil.dp2px(30), DensityUtil.dp2px(11));
            ImageSpan span = new CenterAlignImageSpan(drawable);
            levelIcon.setSpan(span, 0, levelIcon.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }


//        Resources res = context.getResources();
//        Drawable levelIconDrawable = res.getDrawable(resourceId);
//        if(levelIconDrawable == null){
//            return levelIcon;
//        }
//        int originWidth = levelIconDrawable.getIntrinsicWidth();
//        int originHeight = levelIconDrawable.getIntrinsicHeight();
//        float dpHeight = ImageUrl.IMAGE_HIGHT;
//        float dpWidth = originWidth * dpHeight / originHeight;
//        levelIconDrawable.setBounds(0, 0, DensityUtil.dp2px(dpWidth), DensityUtil.dp2px(dpHeight));
//        CenterAlignImageSpan imageSpan = new CenterAlignImageSpan(levelIconDrawable);
//        levelIcon.setSpan(imageSpan,0,levelIcon.length(),
//                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return levelIcon;
    }

}
