package com.whzl.mengbi.model.entity;

import java.util.List;

/**
 * @author nobody
 * @date 2018/10/17
 */
public class PackPrettyBean {


    public List<ListBean> list;

    public static class ListBean {
        /**
         * goodsSn : 2094
         * goodsId : 94451
         * goodsType : PRETTY_NUM
         * goodsName : 7891234
         * isEquip : T
         * surplusDay : 68
         */

        public long goodsSn;
        public int goodsId;
        public String goodsType;
        public String goodsName;
        public String isEquip;
        public int surplusDay;
        public String goodsColor;
    }
}
