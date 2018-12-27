package com.whzl.mengbi.model.entity;

import java.util.List;

/**
 * @author cliang
 * @date 2018.12.27
 */
public class WeekStarRankListBean {
    /**
     * gift : {"goodsName":"幸运520","pic":"http://localtest-img.mengbitv.com/default/000/00/03/47.jpg"}
     * rankTargetType : ANCHOR
     * rankList : [{"userId":30000021,"nickname":"土豪30000021","avatar":"http://localtest-img.mengbitv.com/avatar/030/00/00/21_100x100.jpg?1524652399","value":11},{"userId":30000139,"nickname":"你一定要幸福哦哈嘿嗯","avatar":"http://localtest-img.mengbitv.com/avatar/030/00/01/39_100x100.jpg?1542937458","value":7},{"userId":30000536,"nickname":"萌友30000536","avatar":"http://localtest-img.mengbitv.com/avatar/030/00/05/36_100x100.jpg?1531102704","value":5},{"userId":30000098,"nickname":"yyyyy","avatar":"http://localtest-img.mengbitv.com/avatar/030/00/00/98_100x100.jpg?1526028719","value":4}]
     */

    private GiftBean gift;
    private String rankTargetType;
    private List<RankListBean> rankList;

    public GiftBean getGift() {
        return gift;
    }

    public void setGift(GiftBean gift) {
        this.gift = gift;
    }

    public String getRankTargetType() {
        return rankTargetType;
    }

    public void setRankTargetType(String rankTargetType) {
        this.rankTargetType = rankTargetType;
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

        private String goodsName;
        private String pic;

        public String getGoodsName() {
            return goodsName;
        }

        public void setGoodsName(String goodsName) {
            this.goodsName = goodsName;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }
    }

    public static class RankListBean {
        /**
         * userId : 30000021
         * nickname : 土豪30000021
         * avatar : http://localtest-img.mengbitv.com/avatar/030/00/00/21_100x100.jpg?1524652399
         * value : 11
         */

        public int userId;
        public String nickname;
        public String avatar;
        public int value;
    }
}
