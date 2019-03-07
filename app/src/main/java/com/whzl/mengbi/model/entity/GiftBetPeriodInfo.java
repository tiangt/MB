package com.whzl.mengbi.model.entity;

/**
 * @author nobody
 * @date 2019/3/7
 */
public class GiftBetPeriodInfo {


    /**
     * surplusSecond : 164
     * prizePoolNumber : 0
     * periodNumber : 20190307-310
     * goodsId : 94320
     * robStatus : BETTING
     * uRobGame : {"id":1,"wealthType":"MENG_DOU","amount":100,"limit":10,"createTime":"2019-03-05 09:35:06"}
     * periodId : 1385
     * goodsPic : https://test-img.mengbitv.com/default/000/00/03/47_144x144.jpg
     * userBetCount : 0
     */

    public long surplusSecond;
    public long prizePoolNumber;
    public String periodNumber;
    public int goodsId;
    public String robStatus;
    public URobGameBean uRobGame;
    public int periodId;
    public String goodsPic;
    public int userBetCount;

    public static class URobGameBean {
        /**
         * id : 1
         * wealthType : MENG_DOU
         * amount : 100
         * limit : 10
         * createTime : 2019-03-05 09:35:06
         */

        public int id;
        public String wealthType;
        public int amount;
        public int limit;
        public String createTime;
    }
}
