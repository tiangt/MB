package com.whzl.mengbi.chat.room.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;

import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.whzl.mengbi.chat.room.message.messageJson.FromJson;

import java.util.List;



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

    //根据levelicon资源获取spanableString
    public static SpannableString getLevelSpan(int level, Context context, int resourceId) {
        SpannableString levelIcon = new SpannableString("levelIcon");
        Resources res = context.getResources();
        Drawable levelIconDrawable = res.getDrawable(resourceId);

        if(levelIconDrawable == null){
            return levelIcon;
        }
        int originWidth = levelIconDrawable.getIntrinsicWidth();
        int originHeight = levelIconDrawable.getIntrinsicHeight();

        levelIconDrawable.setBounds(0,0,DensityUtil.dp2px(originWidth),DensityUtil.dp2px(originHeight));
        levelIcon.setSpan(new ImageSpan(levelIconDrawable),0,levelIcon.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return levelIcon;
    }

}
