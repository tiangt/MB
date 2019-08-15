package com.whzl.mengbi.util;

import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.whzl.mengbi.R;
import com.whzl.mengbi.ui.common.BaseApplication;

import java.util.HashMap;
import java.util.Map;

public class PkQualifyingLevelUtils {
    private Map<Integer, Integer> usersLevelMap = new HashMap<>(); //pk排位等级

    private static class ResourceMapHolder {
        private static final PkQualifyingLevelUtils instance = new PkQualifyingLevelUtils();
    }

    private PkQualifyingLevelUtils() {
        initUserMap();
    }


    public static final PkQualifyingLevelUtils getInstance() {
        return ResourceMapHolder.instance;
    }

    private void initUserMap() {
        usersLevelMap.put(1, R.drawable.qualifying1);
        usersLevelMap.put(2, R.drawable.qualifying2);
        usersLevelMap.put(3, R.drawable.qualifying3);
        usersLevelMap.put(4, R.drawable.qualifying4);
        usersLevelMap.put(5, R.drawable.qualifying5);
        usersLevelMap.put(6, R.drawable.qualifying6);
        usersLevelMap.put(7, R.drawable.qualifying7);
        usersLevelMap.put(8, R.drawable.qualifying8);
        usersLevelMap.put(9, R.drawable.qualifying9);
        usersLevelMap.put(10, R.drawable.qualifying10);
        usersLevelMap.put(11, R.drawable.qualifying11);
        usersLevelMap.put(12, R.drawable.qualifying12);
        usersLevelMap.put(13, R.drawable.qualifying13);
        usersLevelMap.put(14, R.drawable.qualifying14);
        usersLevelMap.put(15, R.drawable.qualifying15);
        usersLevelMap.put(16, R.drawable.qualifying16);
        usersLevelMap.put(17, R.drawable.qualifying17);
        usersLevelMap.put(18, R.drawable.qualifying18);
        usersLevelMap.put(19, R.drawable.qualifying19);
        usersLevelMap.put(20, R.drawable.qualifying20);
        usersLevelMap.put(21, R.drawable.qualifying21);
        usersLevelMap.put(22, R.drawable.qualifying22);
        usersLevelMap.put(23, R.drawable.qualifying23);
        usersLevelMap.put(24, R.drawable.qualifying24);
        usersLevelMap.put(25, R.drawable.qualifying25);
    }

    public int getUserLevelIcon(int level) {
        int levelIcon;
        if (usersLevelMap.containsKey(level)) {
            levelIcon = usersLevelMap.get(level);
        } else {
            if (level > 25) {
                levelIcon = R.drawable.qualifying26;
            } else {
                levelIcon = R.drawable.qualifying1;
            }
        }
        return levelIcon;
    }

    public void measureImage(ImageView imageView, int id, LinearLayout linearLayout) {
        linearLayout.removeAllViews();

        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        if (id > 25) {
            layoutParams.width = UIUtil.dip2px(BaseApplication.getInstance(), 57f);
            layoutParams.height = UIUtil.dip2px(BaseApplication.getInstance(), 47f);
            imageView.setLayoutParams(layoutParams);

            String s = String.valueOf(id - 25);
            char[] chars = s.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                ImageView sumImg = new ImageView(linearLayout.getContext());
                ViewGroup.LayoutParams layoutParams1 = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, UIUtil.dip2px(BaseApplication.getInstance(), 11f));
                sumImg.setLayoutParams(layoutParams1);
                char aChar = chars[i];
                if (aChar == '0') {
                    sumImg.setImageResource(R.drawable.num_qualifying_0);
                }
                if (aChar == '1') {
                    sumImg.setImageResource(R.drawable.num_qualifying_1);
                }
                if (aChar == '2') {
                    sumImg.setImageResource(R.drawable.num_qualifying_2);
                }
                if (aChar == '3') {
                    sumImg.setImageResource(R.drawable.num_qualifying_3);
                }
                if (aChar == '4') {
                    sumImg.setImageResource(R.drawable.num_qualifying_4);
                }
                if (aChar == '5') {
                    sumImg.setImageResource(R.drawable.num_qualifying_5);
                }
                if (aChar == '6') {
                    sumImg.setImageResource(R.drawable.num_qualifying_6);
                }
                if (aChar == '7') {
                    sumImg.setImageResource(R.drawable.num_qualifying_7);
                }
                if (aChar == '8') {
                    sumImg.setImageResource(R.drawable.num_qualifying_8);
                }
                if (aChar == '9') {
                    sumImg.setImageResource(R.drawable.num_qualifying_9);
                }
                linearLayout.addView(sumImg);
            }
            return;
        }
        if (id > 20) {
            layoutParams.width = UIUtil.dip2px(BaseApplication.getInstance(), 54f);
            layoutParams.height = UIUtil.dip2px(BaseApplication.getInstance(), 41f);
            imageView.setLayoutParams(layoutParams);
            return;
        }
        if (id > 15) {
            layoutParams.width = UIUtil.dip2px(BaseApplication.getInstance(), 47f);
            layoutParams.height = UIUtil.dip2px(BaseApplication.getInstance(), 36f);
            imageView.setLayoutParams(layoutParams);
            return;
        }
        if (id > 10) {
            layoutParams.width = UIUtil.dip2px(BaseApplication.getInstance(), 47f);
            layoutParams.height = UIUtil.dip2px(BaseApplication.getInstance(), 36f);
            imageView.setLayoutParams(layoutParams);
            return;
        }
        if (id > 5) {
            layoutParams.width = UIUtil.dip2px(BaseApplication.getInstance(), 44f);
            layoutParams.height = UIUtil.dip2px(BaseApplication.getInstance(), 36f);
            imageView.setLayoutParams(layoutParams);
            return;
        }
        if (id > 0) {
            layoutParams.width = UIUtil.dip2px(BaseApplication.getInstance(), 32f);
            layoutParams.height = UIUtil.dip2px(BaseApplication.getInstance(), 36f);
            imageView.setLayoutParams(layoutParams);
            return;
        }

    }

}
