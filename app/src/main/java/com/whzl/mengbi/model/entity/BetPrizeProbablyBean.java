package com.whzl.mengbi.model.entity;

import java.util.List;

/**
 * @author nobody
 * @date 2019-11-26
 */
public class BetPrizeProbablyBean {


    public List<ListBean> list;

    public static class ListBean {
        /**
         * giftName : 幸运520
         * probability : 0.1%-100%
         */

        public String giftName;
        public String probability;
    }
}
