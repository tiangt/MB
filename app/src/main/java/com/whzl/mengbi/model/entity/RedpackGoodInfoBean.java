package com.whzl.mengbi.model.entity;

import java.util.List;

/**
 * @author nobody
 * @date 2019-09-10
 */
public class RedpackGoodInfoBean {


    public List<ConditionGoodListBean> conditionGoodList;
    public List<PrizeGoodsListBean> prizeGoodsList;

    public static class ConditionGoodListBean {
        /**
         * conditionGoodsCfgId : 1
         * goodsId : 94329
         * goodsType : GOODS
         * goodsName : 静一静
         */

        public int conditionGoodsCfgId;
        public int goodsId;
        public String goodsType;
        public String goodsName;
    }

    public static class PrizeGoodsListBean {
        /**
         * goodsPrice : 100
         * minNum : 1000
         * multipleNum : 100
         * prizeGoodsCfgId : 2
         */

        public int goodsPrice;
        public int minNum;
        public int multipleNum;
        public int prizeGoodsCfgId;
        public String goodsName;
    }
}
