package com.whzl.mengbi.model.entity;

/**
 * @author nobody
 * @date 2018/12/5
 */
public class DemonCarBean {

    /**
     * goodsId : 94537
     * goodsEngName : demoncard
     * goodsName : 妖姬卡
     * goodsType : DEMON_CARD
     * prices : {"month":{"priceId":3618,"rent":10000}}
     */

    public long goodsId;
    public String goodsEngName;
    public String goodsName;
    public String goodsType;
    public PricesBean prices;

    public static class PricesBean {
        /**
         * month : {"priceId":3618,"rent":10000}
         */

        public MonthBean month;

        public static class MonthBean {
            /**
             * priceId : 3618
             * rent : 10000
             */

            public int priceId;
            public int rent;
        }
    }
}
