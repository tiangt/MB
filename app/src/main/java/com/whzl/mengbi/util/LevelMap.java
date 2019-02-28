package com.whzl.mengbi.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author cliang
 * @date 2019.2.19
 */
public class LevelMap {

    private Map<Integer, String> usersLevelMap = new HashMap<>(); //用户等级
    private Map<Integer, String> anchorLevelMap = new HashMap<>(); //主播等级
    private Map<Integer, String> royalLevelMap = new HashMap<>(); //贵族等级

    private static class LevelMapHolder {
        private static final LevelMap instance = new LevelMap();
    }

    private LevelMap(){
        initUserMap();
        initAnchorMap();
        initRoyalMap();
    }

    public static final LevelMap getLevelMap() {
        return LevelMapHolder.instance;
    }

    private void initUserMap() {
        usersLevelMap.put(1, "平民");
        usersLevelMap.put(2, "名士1");
        usersLevelMap.put(3, "名士2");
        usersLevelMap.put(4, "名士3");
        usersLevelMap.put(5, "名士4");
        usersLevelMap.put(6, "名士5");
        usersLevelMap.put(7, "富豪6");
        usersLevelMap.put(8, "富豪7");
        usersLevelMap.put(9, "富豪8");
        usersLevelMap.put(10, "富豪9");
        usersLevelMap.put(11, "富豪10");
        usersLevelMap.put(12, "富商11");
        usersLevelMap.put(13, "富商12");
        usersLevelMap.put(14, "富商13");
        usersLevelMap.put(15, "富商14");
        usersLevelMap.put(16, "富商15");
        usersLevelMap.put(17, "富甲16");
        usersLevelMap.put(18, "富甲17");
        usersLevelMap.put(19, "富甲18");
        usersLevelMap.put(20, "富甲19");
        usersLevelMap.put(21, "富甲20");
        usersLevelMap.put(22, "男爵");
        usersLevelMap.put(23, "子爵");
        usersLevelMap.put(24, "伯爵");
        usersLevelMap.put(25, "侯爵");
        usersLevelMap.put(26, "公爵");
        usersLevelMap.put(27, "大公");
        usersLevelMap.put(28, "囯公");
        usersLevelMap.put(29, "囯师");
        usersLevelMap.put(30, "储王");
        usersLevelMap.put(31, "郡王");
        usersLevelMap.put(32, "藩王");
        usersLevelMap.put(33, "亲王");
        usersLevelMap.put(34, "国王");
        usersLevelMap.put(35, "神");
        usersLevelMap.put(36, "神尊");
        usersLevelMap.put(37, "至尊天下");
        usersLevelMap.put(38, "至尊天下");
    }

    private void initAnchorMap() {
        anchorLevelMap.put(1, "爱心1");
        anchorLevelMap.put(2, "爱心2");
        anchorLevelMap.put(3, "爱心3");
        anchorLevelMap.put(4, "爱心4");
        anchorLevelMap.put(5, "爱心5");
        anchorLevelMap.put(6, "钻石1");
        anchorLevelMap.put(7, "钻石2");
        anchorLevelMap.put(8, "钻石3");
        anchorLevelMap.put(9, "钻石4");
        anchorLevelMap.put(10, "钻石5");
        anchorLevelMap.put(11, "钻石6");
        anchorLevelMap.put(12, "钻石7");
        anchorLevelMap.put(13, "钻石8");
        anchorLevelMap.put(14, "钻石9");
        anchorLevelMap.put(15, "钻石10");
        anchorLevelMap.put(16, "皇冠1");
        anchorLevelMap.put(17, "皇冠2");
        anchorLevelMap.put(18, "皇冠3");
        anchorLevelMap.put(19, "皇冠4");
        anchorLevelMap.put(20, "皇冠5");
        anchorLevelMap.put(21, "皇冠6");
        anchorLevelMap.put(22, "皇冠7");
        anchorLevelMap.put(23, "皇冠8");
        anchorLevelMap.put(24, "皇冠9");
        anchorLevelMap.put(25, "皇冠10");
        anchorLevelMap.put(26, "传奇1");
        anchorLevelMap.put(27, "传奇2");
        anchorLevelMap.put(28, "传奇3");
        anchorLevelMap.put(29, "传奇4");
        anchorLevelMap.put(30, "传奇5");
        anchorLevelMap.put(31, "传奇6");
        anchorLevelMap.put(32, "传奇7");
        anchorLevelMap.put(33, "传奇8");
        anchorLevelMap.put(34, "传奇9");
        anchorLevelMap.put(35, "传奇10");
        anchorLevelMap.put(36, "传奇11");
        anchorLevelMap.put(37, "传奇12");
        anchorLevelMap.put(38, "传奇13");
        anchorLevelMap.put(39, "传奇14");
        anchorLevelMap.put(40, "传奇15");
        anchorLevelMap.put(41, "殿堂1");
        anchorLevelMap.put(42, "殿堂2");
        anchorLevelMap.put(43, "殿堂3");
        anchorLevelMap.put(44, "殿堂4");
        anchorLevelMap.put(45, "殿堂5");
        anchorLevelMap.put(46, "殿堂6");
        anchorLevelMap.put(47, "殿堂7");
        anchorLevelMap.put(48, "殿堂8");
        anchorLevelMap.put(49, "殿堂9");
        anchorLevelMap.put(50, "殿堂10");
    }

    private void initRoyalMap() {
        royalLevelMap.put(1, "青铜");
        royalLevelMap.put(2, "白银");
        royalLevelMap.put(3, "黄金");
        royalLevelMap.put(4, "铂金");
        royalLevelMap.put(5, "钻石");
        royalLevelMap.put(6, "星耀");
        royalLevelMap.put(7, "王者");
        royalLevelMap.put(8, "传说");
    }

    public String getUserLevel(int level) {
        String levelName = "";
        if (usersLevelMap.containsKey(level)) {
            levelName = usersLevelMap.get(level);
        } else {
            if (level > 37) {
                levelName = "至尊天下";
            } else {
                levelName = "平民";
            }
        }
        return levelName;
    }

    public String getAnchorLevel(int anchorLevel) {
        String levelName = "";
        if (anchorLevelMap.containsKey(anchorLevel)) {
            levelName = anchorLevelMap.get(anchorLevel);
        } else {
            if (anchorLevel > 49) {
                levelName = "殿堂10";
            } else {
                levelName = "爱心1";
            }
        }
        return levelName;
    }

    public String getRoyalLevel(int level) {
        String levelName = "";
        if (royalLevelMap.containsKey(level)) {
            levelName = royalLevelMap.get(level);
        } else {
            if(level > 7){
                levelName = "传说";
            }else{
                levelName = "无等级";
            }
        }
        return levelName;
    }
}
