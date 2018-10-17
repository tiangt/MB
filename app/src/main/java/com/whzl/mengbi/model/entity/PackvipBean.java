package com.whzl.mengbi.model.entity;

import java.util.List;

/**
 * @author nobody
 * @date 2018/10/17
 */
public class PackvipBean {

    public List<ListBean> list;

    public static class ListBean {
        /**
         * goodsSn : 2092
         * goodsId : 301
         * goodsType : VIP
         * goodsName : vip
         * surplusDay : 368
         * awardReceived : false
         */

        public long goodsSn;
        public int goodsId;
        public String goodsType;
        public String goodsName;
        public int surplusDay;
        public boolean awardReceived;
    }
}
