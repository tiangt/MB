package com.whzl.mengbi.model.entity;

import java.util.List;

/**
 * @author nobody
 * @date 2018/10/16
 */
public class PropBean {


    public List<ListBean> list;

    public static class ListBean {
        /**
         * goodsSn : 2515
         * goodsId : 200
         * goodsType : SERVICE
         * goodsName : 广播
         * goodsSum : 20
         * surplusDay : 0
         */

        public long goodsSn;
        public int goodsId;
        public String goodsType;
        public String goodsName;
        public int goodsSum;
        public int surplusDay;
    }
}
