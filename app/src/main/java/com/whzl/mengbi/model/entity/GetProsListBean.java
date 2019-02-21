package com.whzl.mengbi.model.entity;

import java.util.List;

/**
 * @author nobody
 * @date 2019/2/21
 */
public class GetProsListBean {


    public List<ListBean> list;

    public static class ListBean {
        /**
         * rent : 10000
         * priceId : 19
         * sum : 1
         * goodsId : 200
         * goodsEngName : boardcast
         * goodsName : 广播
         * goodsType : SERVICE
         * goodsPic : https://test-img.mengbitv.com/default/000/00/05/41_144x144.jpg
         * userExp : 0
         * anchorExp : 0
         * days : 30
         * givingMengDou : 10000
         */

        public long rent;
        public int priceId;
        public int sum;
        public int goodsId;
        public String goodsEngName;
        public String goodsName;
        public String goodsType;
        public String goodsPic;
        public String remark;
        public int userExp;
        public int anchorExp;
        public int days;
        public long givingMengDou;
    }
}
