package com.whzl.mengbi.util;

import android.view.ViewGroup;
import android.widget.ImageView;

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

    public void measureImage(ImageView imageView, int id) {
        ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
        if (id > 25) {
            layoutParams.width = UIUtil.dip2px(BaseApplication.getInstance(), 57f);
            layoutParams.height = UIUtil.dip2px(BaseApplication.getInstance(), 47f);
            imageView.setLayoutParams(layoutParams);
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
