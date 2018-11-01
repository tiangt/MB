package com.whzl.mengbi.model.entity;

import java.util.List;

/**
 * @author nobody
 * @date 2018/10/24
 */
public class BillAwardBean {

    public List<ListBean> list;

    public static class ListBean {
        /**
         * awardSn : 143251
         * awardName : 新人奖励送瓜
         * contentDetailName : 萌币+1000;
         * statusDate : 2018-10-23 11:06:46
         */

        public long awardSn;
        public String awardName;
        public String contentDetailName;
        public String statusDate;
    }
}
