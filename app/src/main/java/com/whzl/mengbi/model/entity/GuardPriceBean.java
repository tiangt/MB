package com.whzl.mengbi.model.entity;

/**
 * @author shaw
 * @date 2018/8/6
 */
public class GuardPriceBean {

    /**
     * goodsId : 300
     * goodsEngName : guard
     * goodsName : 守护
     * goodsType : GUARD
     * prices : {"month":{"priceId":18,"rent":30000}}
     */

    public int goodsId;
    public String goodsEngName;
    public String goodsName;
    public String goodsType;
    public PricesDetailBean prices;

    public class PricesDetailBean {
        /**
         * month : {"priceId":18,"rent":30000}
         */

        public MonthBean month;
    }

    public class MonthBean {
        /**
         * priceId : 18
         * rent : 30000
         */

        public int priceId;
        public long rent;
    }
}
