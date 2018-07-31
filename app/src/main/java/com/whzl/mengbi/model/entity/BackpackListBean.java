package com.whzl.mengbi.model.entity;

import java.util.List;

/**
 * @author shaw
 * @date 2018/7/31
 */
public class BackpackListBean {

    public List<GoodsDetailBean> list;

    public class GoodsDetailBean {
        /**
         * goodsId : 94334
         * goodsName : 亲一亲
         * goodsPic : http://test-img.mengbitv.com/default/000/00/01/23.jpg
         * count : 5
         */

        public int goodsId;
        public String goodsName;
        public String goodsPic;
        public int count;
    }
}
