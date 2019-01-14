package com.whzl.mengbi.model.entity;

import java.util.List;

/**
 * @author nobody
 * @date 2019/1/14
 */
public class GetUserSetBean {

    public List<ListBean> list;

    public static class ListBean {
        /**
         * setType : private
         * setValue : 1
         */

        public String setType;
        public String setValue;
    }
}
