package com.whzl.mengbi.model.entity;

import java.util.List;

/**
 * @author cliang
 * @date 2018.12.27
 */
public class WeekStarRankListBean {
    /**
     * gift : {"goodsName":"幸运520","pic":"http://localtest-img.mengbitv.com/default/000/00/03/47.jpg"}
     * rankTargetType :
     * rankList : [{"userId":30000012,"nickname":"修改昵称","avatar":"http://localtest-img.mengbitv.com/avatar/030/00/00/12_100x100.jpg?1529897703","userLevelMap":{"ANCHOR_LEVEL":35,"ROYAL_LEVEL":0,"USER_LEVEL":21},"value":2600},{"userId":30000043,"nickname":"土豪30000043","avatar":"http://localtest-img.mengbitv.com/avatar/030/00/00/43_100x100.jpg?1524657350","userLevelMap":{"ANCHOR_LEVEL":16,"ROYAL_LEVEL":0,"USER_LEVEL":1},"value":1584},{"userId":30000077,"nickname":"土豪30000077","avatar":"http://localtest-img.mengbitv.com/avatar/030/00/00/77_100x100.jpg?1524657199","userLevelMap":{"ANCHOR_LEVEL":17,"ROYAL_LEVEL":0,"USER_LEVEL":1},"value":577},{"userId":30000150,"nickname":"美丽","avatar":"http://localtest-img.mengbitv.com/avatar/030/00/01/50_100x100.jpg?1542613077","userLevelMap":{"ANCHOR_LEVEL":22,"ROYAL_LEVEL":7,"USER_LEVEL":28},"value":356},{"userId":30000536,"nickname":"萌友30000536","avatar":"http://localtest-img.mengbitv.com/avatar/030/00/05/36_100x100.jpg?1531102704","userLevelMap":{"ANCHOR_LEVEL":21,"ROYAL_LEVEL":0,"USER_LEVEL":1},"value":74}]
     * rankId : 37060
     */

    public GiftBean gift;
    public String rankTargetType;
    public String rankId;
    public List<RankListBean> rankList;

    public GiftBean getGift() {
        return gift;
    }

    public void setGift(GiftBean gift) {
        this.gift = gift;
    }

    public List<RankListBean> getRankList() {
        return rankList;
    }

    public void setRankList(List<RankListBean> rankList) {
        this.rankList = rankList;
    }

    public static class GiftBean {
        /**
         * goodsName : 幸运520
         * pic : http://localtest-img.mengbitv.com/default/000/00/03/47.jpg
         */

        public String goodsName;
        public String pic;
    }

    public static class RankListBean {
        /**
         * userId : 30000012
         * nickname : 修改昵称
         * avatar : http://localtest-img.mengbitv.com/avatar/030/00/00/12_100x100.jpg?1529897703
         * userLevelMap : {"ANCHOR_LEVEL":35,"ROYAL_LEVEL":0,"USER_LEVEL":21}
         * value : 2600
         */

        public int userId;
        public String nickname;
        public String avatar;
        private UserLevelMapBean userLevelMap;
        public int value;

        public UserLevelMapBean getUserLevelMap() {
            return userLevelMap;
        }

        public void setUserLevelMap(UserLevelMapBean userLevelMap) {
            this.userLevelMap = userLevelMap;
        }

        public static class UserLevelMapBean {
            /**
             * ANCHOR_LEVEL : 35
             * ROYAL_LEVEL : 0
             * USER_LEVEL : 21
             */
            public int ANCHOR_LEVEL;
            public int ROYAL_LEVEL;
            public int USER_LEVEL;
        }
    }
}
