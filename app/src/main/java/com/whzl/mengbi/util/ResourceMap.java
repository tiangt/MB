package com.whzl.mengbi.util;

import com.whzl.mengbi.R;

import java.util.HashMap;
import java.util.Map;

public class ResourceMap {
    private Map<Integer,Integer> usersLevelMap = new HashMap<>();
    private Map<Integer,Integer> anchorLevelMap = new HashMap<>();

    private static class ResourceMapHolder {
        private static final ResourceMap instance = new ResourceMap();
    }
    private ResourceMap(){
        initUserMap();
        initAnchorMap();
    }
    public static final ResourceMap getResourceMap(){
        return ResourceMapHolder.instance;
    }

    private void initUserMap() {
        usersLevelMap.put(0, R.drawable.usergrade);
        usersLevelMap.put(1, R.drawable.usergrade1);
        usersLevelMap.put(2, R.drawable.usergrade2);
        usersLevelMap.put(3, R.drawable.usergrade3);
        usersLevelMap.put(4, R.drawable.usergrade4);
        usersLevelMap.put(5, R.drawable.usergrade5);
        usersLevelMap.put(6, R.drawable.usergrade6);
        usersLevelMap.put(7, R.drawable.usergrade7);
        usersLevelMap.put(8, R.drawable.usergrade8);
        usersLevelMap.put(9, R.drawable.usergrade9);
        usersLevelMap.put(10, R.drawable.usergrade10);
        usersLevelMap.put(11, R.drawable.usergrade11);
        usersLevelMap.put(12, R.drawable.usergrade12);
        usersLevelMap.put(13, R.drawable.usergrade13);
        usersLevelMap.put(14, R.drawable.usergrade14);
        usersLevelMap.put(15, R.drawable.usergrade15);
        usersLevelMap.put(16, R.drawable.usergrade16);
        usersLevelMap.put(17, R.drawable.usergrade17);
        usersLevelMap.put(18, R.drawable.usergrade18);
        usersLevelMap.put(19, R.drawable.usergrade19);
        usersLevelMap.put(20, R.drawable.usergrade20);
        usersLevelMap.put(21, R.drawable.usergrade21);
        usersLevelMap.put(22, R.drawable.usergrade22);
        usersLevelMap.put(23, R.drawable.usergrade23);
        usersLevelMap.put(24, R.drawable.usergrade24);
        usersLevelMap.put(25, R.drawable.usergrade25);
        usersLevelMap.put(26, R.drawable.usergrade26);
        usersLevelMap.put(27, R.drawable.usergrade27);
        usersLevelMap.put(28, R.drawable.usergrade28);
        usersLevelMap.put(29, R.drawable.usergrade29);
        usersLevelMap.put(30, R.drawable.usergrade30);
        usersLevelMap.put(31, R.drawable.usergrade31);
        usersLevelMap.put(32, R.drawable.usergrade32);
        usersLevelMap.put(33, R.drawable.usergrade33);
        usersLevelMap.put(34, R.drawable.usergrade34);
        usersLevelMap.put(35, R.drawable.usergrade35);
        usersLevelMap.put(36, R.drawable.usergrade36);
        usersLevelMap.put(37, R.drawable.usergrade37);
    }

    private void initAnchorMap() {

    }

    public int getUserLevelIcon(int level) {
        int levelIcon;
        if (usersLevelMap.containsKey(level)) {
            levelIcon = usersLevelMap.get(level);
        }else {
            if (level > 37) {
                levelIcon = R.drawable.usergrade37;
            }else {
                levelIcon = R.drawable.usergrade;
            }
        }
        return levelIcon;
    }

}
