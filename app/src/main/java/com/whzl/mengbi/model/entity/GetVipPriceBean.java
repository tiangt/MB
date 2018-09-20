package com.whzl.mengbi.model.entity;

/**
 * @author nobody
 * @date 2018/9/20
 */
public class GetVipPriceBean {

    /**
     * goodsId : 301
     * goodsEngName : vip
     * goodsName : vip
     * goodsType : VIP
     * prices : {"month":{"priceId":3517,"rent":30000}}
     */

    public int goodsId;
    public String goodsEngName;
    public String goodsName;
    public String goodsType;
    public PricesBean prices;

    public static class PricesBean {
        /**
         * month : {"priceId":3517,"rent":30000}
         */

        public MonthBean month;

        public static class MonthBean {
            /**
             * priceId : 3517
             * rent : 30000
             */

            public int priceId;
            public int rent;
        }
    }
}
