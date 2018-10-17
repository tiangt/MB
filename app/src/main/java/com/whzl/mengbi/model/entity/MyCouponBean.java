package com.whzl.mengbi.model.entity;

import java.util.List;

/**
 * @author nobody
 * @date 2018/10/17
 */
public class MyCouponBean  {

    public List<ListBean> list;

    public static class ListBean {
        /**
         * goodsSn : 2521
         * goodsId : 94338
         * goodsType : COUPON
         * goodsName : 3%返利券
         * goodsPic : https://test-img.mengbitv.com/default/000/00/01/48.jpg
         * identifyCode : ZWEXOB
         * surplusDay : 21
         */

        public long goodsSn;
        public int goodsId;
        public String goodsType;
        public String goodsName;
        public String goodsPic;
        public String identifyCode;
        public int surplusDay;
        public int goodsSum;
    }
}
