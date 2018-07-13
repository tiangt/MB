package com.whzl.mengbi.util;

import com.whzl.mengbi.R;

import java.util.HashMap;
import java.util.Map;

public class ResourceMap {
    private Map<Integer,Integer> usersLevelMap = new HashMap<>(); //用户等级
    private Map<Integer,Integer> anchorLevelMap = new HashMap<>(); //主播等级

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
        usersLevelMap.put(1, R.drawable.usergrade);
        usersLevelMap.put(2, R.drawable.usergrade1);
        usersLevelMap.put(3, R.drawable.usergrade2);
        usersLevelMap.put(4, R.drawable.usergrade3);
        usersLevelMap.put(5, R.drawable.usergrade4);
        usersLevelMap.put(6, R.drawable.usergrade5);
        usersLevelMap.put(7, R.drawable.usergrade6);
        usersLevelMap.put(8, R.drawable.usergrade7);
        usersLevelMap.put(9, R.drawable.usergrade8);
        usersLevelMap.put(10, R.drawable.usergrade9);
        usersLevelMap.put(11, R.drawable.usergrade10);
        usersLevelMap.put(12, R.drawable.usergrade11);
        usersLevelMap.put(13, R.drawable.usergrade12);
        usersLevelMap.put(14, R.drawable.usergrade13);
        usersLevelMap.put(15, R.drawable.usergrade14);
        usersLevelMap.put(16, R.drawable.usergrade15);
        usersLevelMap.put(17, R.drawable.usergrade16);
        usersLevelMap.put(18, R.drawable.usergrade17);
        usersLevelMap.put(19, R.drawable.usergrade18);
        usersLevelMap.put(20, R.drawable.usergrade19);
        usersLevelMap.put(21, R.drawable.usergrade20);
        usersLevelMap.put(22, R.drawable.usergrade21);
        usersLevelMap.put(23, R.drawable.usergrade22);
        usersLevelMap.put(24, R.drawable.usergrade23);
        usersLevelMap.put(25, R.drawable.usergrade24);
        usersLevelMap.put(26, R.drawable.usergrade25);
        usersLevelMap.put(27, R.drawable.usergrade26);
        usersLevelMap.put(28, R.drawable.usergrade27);
        usersLevelMap.put(29, R.drawable.usergrade28);
        usersLevelMap.put(30, R.drawable.usergrade29);
        usersLevelMap.put(31, R.drawable.usergrade30);
        usersLevelMap.put(32, R.drawable.usergrade31);
        usersLevelMap.put(33, R.drawable.usergrade32);
        usersLevelMap.put(34, R.drawable.usergrade33);
        usersLevelMap.put(35, R.drawable.usergrade34);
        usersLevelMap.put(36, R.drawable.usergrade35);
        usersLevelMap.put(37, R.drawable.usergrade36);
        usersLevelMap.put(38, R.drawable.usergrade37);
    }

    private void initAnchorMap() {
        anchorLevelMap.put(1, R.drawable.anchor_level1);
        anchorLevelMap.put(2, R.drawable.anchor_level2);
        anchorLevelMap.put(3, R.drawable.anchor_level3);
        anchorLevelMap.put(4, R.drawable.anchor_level4);
        anchorLevelMap.put(5, R.drawable.anchor_level5);
        anchorLevelMap.put(6, R.drawable.anchor_level6);
        anchorLevelMap.put(7, R.drawable.anchor_level7);
        anchorLevelMap.put(8, R.drawable.anchor_level8);
        anchorLevelMap.put(9, R.drawable.anchor_level9);
        anchorLevelMap.put(10, R.drawable.anchor_level10);
        anchorLevelMap.put(11, R.drawable.anchor_level11);
        anchorLevelMap.put(12, R.drawable.anchor_level12);
        anchorLevelMap.put(13, R.drawable.anchor_level13);
        anchorLevelMap.put(14, R.drawable.anchor_level14);
        anchorLevelMap.put(15, R.drawable.anchor_level15);
        anchorLevelMap.put(16, R.drawable.anchor_level16);
        anchorLevelMap.put(17, R.drawable.anchor_level17);
        anchorLevelMap.put(18, R.drawable.anchor_level18);
        anchorLevelMap.put(19, R.drawable.anchor_level19);
        anchorLevelMap.put(20, R.drawable.anchor_level20);
        anchorLevelMap.put(21, R.drawable.anchor_level21);
        anchorLevelMap.put(22, R.drawable.anchor_level22);
        anchorLevelMap.put(23, R.drawable.anchor_level23);
        anchorLevelMap.put(24, R.drawable.anchor_level24);
        anchorLevelMap.put(25, R.drawable.anchor_level25);
        anchorLevelMap.put(26, R.drawable.anchor_level26);
        anchorLevelMap.put(27, R.drawable.anchor_level27);
        anchorLevelMap.put(28, R.drawable.anchor_level28);
        anchorLevelMap.put(29, R.drawable.anchor_level29);
        anchorLevelMap.put(30, R.drawable.anchor_level30);
        anchorLevelMap.put(31, R.drawable.anchor_level31);
        anchorLevelMap.put(32, R.drawable.anchor_level32);
        anchorLevelMap.put(33, R.drawable.anchor_level33);
        anchorLevelMap.put(34, R.drawable.anchor_level34);
        anchorLevelMap.put(35, R.drawable.anchor_level35);
        anchorLevelMap.put(36, R.drawable.anchor_level36);
        anchorLevelMap.put(37, R.drawable.anchor_level37);
        anchorLevelMap.put(38, R.drawable.anchor_level38);
        anchorLevelMap.put(39, R.drawable.anchor_level39);
        anchorLevelMap.put(40, R.drawable.anchor_level40);
        anchorLevelMap.put(41, R.drawable.anchor_level41);
        anchorLevelMap.put(42, R.drawable.anchor_level42);
        anchorLevelMap.put(43, R.drawable.anchor_level43);
        anchorLevelMap.put(44, R.drawable.anchor_level44);
        anchorLevelMap.put(45, R.drawable.anchor_level45);
        anchorLevelMap.put(46, R.drawable.anchor_level46);
        anchorLevelMap.put(47, R.drawable.anchor_level47);
        anchorLevelMap.put(48, R.drawable.anchor_level48);
        anchorLevelMap.put(49, R.drawable.anchor_level49);
        anchorLevelMap.put(50, R.drawable.anchor_level50);
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

    public int getAnchorLevelIcon(int anchorLevel) {
        int levelIcon;
        if (anchorLevelMap.containsKey(anchorLevel)) {
            levelIcon = anchorLevelMap.get(anchorLevel);
        }else {
            if (anchorLevel > 49) {
                levelIcon = R.drawable.anchor_level50;
            }else {
                levelIcon = R.drawable.anchor_level1;
            }
        }
        return levelIcon;
    }
}
