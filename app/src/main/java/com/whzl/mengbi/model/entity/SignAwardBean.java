package com.whzl.mengbi.model.entity;

import java.util.List;

/**
 * @author nobody
 * @date 2019/5/9
 */
public class SignAwardBean {

    public List<ListBean> list;

    public static class ListBean {
        /**
         * subAwardType :
         * awardType : GOODS
         * num : 5
         * goodsName : 蓝色妖姬
         * effDays : 0
         */

        public String subAwardType;
        public String awardType;
        public int num;
        public String goodsName;
        public int effDays;
    }
}
