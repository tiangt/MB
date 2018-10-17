package com.whzl.mengbi.model.entity;

import java.util.List;

/**
 * @author nobody
 * @date 2018/10/17
 */
public class PackcarBean {

    public List<ListBean> list;

    public static class ListBean {
        /**
         * goodsSn : 2518
         * goodsId : 94314
         * goodsType : CAR
         * goodsName : 首充座驾
         * goodsPic : https://test-img.mengbitv.com/default/000/00/01/10.jpg
         * isEquip : F
         * surplusDay : 21
         */

        public int goodsSn;
        public int goodsId;
        public String goodsType;
        public String goodsName;
        public String goodsPic;
        public String isEquip;
        public int surplusDay;
    }
}
