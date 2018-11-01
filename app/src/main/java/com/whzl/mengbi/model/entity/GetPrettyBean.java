package com.whzl.mengbi.model.entity;

/**
 * @author nobody
 * @date 2018/10/17
 */
public class GetPrettyBean {


    /**
     * goodsId : 94517
     * goodsEngName : 31111
     * goodsName : 31111
     * goodsType : PRETTY_NUM
     * prices : {"month":{"priceId":3585,"rent":500150}}
     */

    public int goodsId;
    public String goodsEngName;
    public String goodsName;
    public String goodsType;
    public PricesBean prices;

    public static class PricesBean {
        /**
         * month : {"priceId":3585,"rent":500150}
         */

        public MonthBean month;

        public static class MonthBean {
            /**
             * priceId : 3585
             * rent : 500150
             */

            public int priceId;
            public int rent;
        }
    }
}
