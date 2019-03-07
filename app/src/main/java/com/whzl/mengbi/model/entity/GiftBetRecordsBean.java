package com.whzl.mengbi.model.entity;

import java.util.List;

/**
 * @author nobody
 * @date 2019/3/7
 */
public class GiftBetRecordsBean {

    public List<ListBean> list;

    public static class ListBean {
        /**
         * nickName : 社会一姐社会一姐社会
         * robNumber : 10
         * goodsName : 666
         * prizeNumber : 10
         * periodNumber : 20190307-293
         */

        public String nickName;
        public long robNumber;
        public String goodsName;
        public long prizeNumber;
        public String periodNumber;
    }
}
