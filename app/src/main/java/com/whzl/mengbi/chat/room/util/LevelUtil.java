package com.whzl.mengbi.chat.room.util;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ImageSpan;
import android.widget.TextView;

import com.scwang.smartrefresh.layout.util.DensityUtil;
import com.whzl.mengbi.chat.room.message.messageJson.FromJson;
import com.whzl.mengbi.model.entity.RoomInfoBean;
import com.whzl.mengbi.util.ResourceMap;
import com.whzl.mengbi.util.UIUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;


public class LevelUtil {
    public static int getUserLevel(FromJson fromJson) {
        if (fromJson == null) {
            return -1;
        }
        List<FromJson.Level> levelList = fromJson.getLevelList();
        if (levelList == null) {
            return -1;
        }
        for (FromJson.Level levelItem : levelList) {
            if (levelItem.getLevelType().equals("USER_LEVEL")) {
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
        for (FromJson.Level levelItem : levelList) {
            if (levelItem.getLevelType().equals("ANCHOR_LEVEL")) {
                int levelVal = levelItem.getLevelValue();
                return levelVal;
            }
        }
        return -1;
    }

    public static int getRoyalLevel(FromJson fromJson) {
        if (fromJson == null) {
            return -1;
        }
        List<FromJson.Level> levelList = fromJson.getLevelList();
        if (levelList == null) {
            return -1;
        }
        for (FromJson.Level levelItem : levelList) {
            if (levelItem.getLevelType().equals("ROYAL_LEVEL")) {
                int levelValue = levelItem.getLevelValue();
                return levelValue;
            }
        }
        return -1;
    }

    public static String getPrettyNumColor(FromJson fromJson) {
        if (fromJson == null) {
            return "E";
        }
        List<FromJson.Good> goodsList = fromJson.getGoodsList();
        if (goodsList == null) {
            return "E";
        }
        for (FromJson.Good good : goodsList) {
            if (good.getGoodsType().equals("PRETTY_NUM") && good.goodsColor != null) {
                String goodsColor = good.goodsColor;
                return goodsColor;
            }
        }
        return "E";
    }

    public static String getPrettyNum(FromJson fromJson) {
        if (fromJson == null) {
            return "";
        }
        List<FromJson.Good> goodsList = fromJson.getGoodsList();
        if (goodsList == null) {
            return "";
        }
        for (FromJson.Good good : goodsList) {
            if (good.getGoodsType().equals("PRETTY_NUM")) {
                String goodsColor = good.getGoodsName();
                return goodsColor;
            }
        }
        return "";
    }

    /**
     * 根据图片资源获取spanString
     *
     * @param context
     * @param resourceId 图片资源
     * @return
     */
    public static SpannableString getImageResourceSpan(Context context, int resourceId) {
        SpannableString levelIcon = new SpannableString("icon");
        Resources res = context.getResources();
        Drawable levelIconDrawable = res.getDrawable(resourceId);
        if (levelIconDrawable == null) {
            return levelIcon;
        }
        int originWidth = levelIconDrawable.getIntrinsicWidth();
        int originHeight = levelIconDrawable.getIntrinsicHeight();
        float dpHeight = ImageUrl.IMAGE_HIGHT;
        float dpWidth = originWidth * dpHeight / originHeight;
        levelIconDrawable.setBounds(0, 0, UIUtil.sp2px(context, dpWidth), UIUtil.sp2px(context, dpHeight));
        ImageSpan imageSpan = new CenterAlignImageSpan(levelIconDrawable);
        levelIcon.setSpan(imageSpan, 0, levelIcon.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return levelIcon;
    }


    /**
     * 根据图片资源获取spanString
     *
     * @param context
     * @param resourceId 图片资源
     * @return
     */
    public static SpannableString getRoyalImageResourceSpan(Context context, int resourceId, TextView textView) throws IOException {
        SpannableString levelIcon = new SpannableString("icon");
        Drawable drawable = null;

        Resources res = context.getResources();
        Bitmap oldBmp = BitmapFactory.decodeResource(res, ResourceMap.getResourceMap().getRoyalLevelIcon(resourceId));
        Bitmap newBmp = Bitmap.createScaledBitmap(oldBmp, UIUtil.dip2px(context, 27.5f), UIUtil.dip2px(context, 10f), true);
        drawable = new BitmapDrawable(res, newBmp);

//        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
//            Bitmap bitmap = FileUtils.readBitmapFromAssetsFile("images/face/royal/royal_" + resourceId + ".gif", context);
//            drawable = new BitmapDrawable(context.getResources(), bitmap);
//            bitmap.recycle();
//        } else {
//            drawable = new GifDrawable(getFileContent(context, "images/face/royal/royal_" + resourceId + ".gif"));
//            drawable.setCallback(new DrawableCallback(textView));
//        }
        if (drawable != null) {
            int originWidth = drawable.getIntrinsicWidth();
            int originHeight = drawable.getIntrinsicHeight();
            float dpHeight = 14;
            float dpWidth = originWidth * dpHeight / originHeight;
            drawable.setBounds(0, 0, UIUtil.sp2px(context, dpWidth), UIUtil.sp2px(context, dpHeight));
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

    private static byte[] getFileContent(Context context, String fileName) {
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

    public static SpannableString getImageResourceSpanByHeight(Context context, int resourceId, int height) {
        SpannableString levelIcon = new SpannableString("icon");
        Resources res = context.getResources();
        Drawable levelIconDrawable = res.getDrawable(resourceId);
        if (levelIconDrawable == null) {
            return levelIcon;
        }
        int originWidth = levelIconDrawable.getIntrinsicWidth();
        int originHeight = levelIconDrawable.getIntrinsicHeight();
        float dpHeight = height;
        float dpWidth = originWidth * dpHeight / originHeight;
        levelIconDrawable.setBounds(0, 0, DensityUtil.dp2px(dpWidth), DensityUtil.dp2px(dpHeight));
        ImageSpan imageSpan = new CenterAlignImageSpan(levelIconDrawable);
        levelIcon.setSpan(imageSpan, 0, levelIcon.length(),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return levelIcon;
    }

    public static int getAnchorLevel(RoomInfoBean.DataBean.AnchorBean level) {
        if (level == null) {
            return -1;
        }
        List<RoomInfoBean.DataBean.AnchorBean.LevelBean> levelBeans = level.getLevel();
        if (levelBeans == null) {
            return -1;
        }
        for (RoomInfoBean.DataBean.AnchorBean.LevelBean bean : levelBeans) {
            if (bean.getLevelType().equals("ANCHOR_LEVEL")) {
                int levelVal = bean.getLevelValue();
                return levelVal;
            }
        }
        return -1;
    }
}
