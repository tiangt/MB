package com.whzl.mengbi.model;

import java.util.List;

/**
 * @author nobody
 * @date 2019-06-19
 */
public class FlopPriceBean {

    public List<ListBean> list;

    public static class ListBean {
        /**
         * number : 8000
         * wealthType : MENG_DOU
         */

        public long number;
        public String wealthType;
    }
}
