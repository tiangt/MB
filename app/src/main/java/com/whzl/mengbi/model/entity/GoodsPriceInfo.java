package com.whzl.mengbi.model.entity;

/**
 * @author cliang
 * @date 2018.12.28
 */
public class GoodsPriceInfo extends ResponseInfo {

    /**
     * data : {"rent":50000,"priceId":3458,"goodsId":94340,"goodsEngName":"caidanzhubo","goodsName":"彩主播","goodsType":"GIFT","userExp":100,"anchorExp":40}
     */

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * rent : 50000
         * priceId : 3458
         * goodsId : 94340
         * goodsEngName : caidanzhubo
         * goodsName : 彩主播
         * goodsType : GIFT
         * userExp : 100
         * anchorExp : 40
         */

        public int rent;
        public int priceId;
        public int goodsId;
        public String goodsEngName;
        public String goodsName;
        public String goodsType;
        public int userExp;
        public int anchorExp;
    }
}
