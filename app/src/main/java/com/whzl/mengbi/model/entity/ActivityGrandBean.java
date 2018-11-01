package com.whzl.mengbi.model.entity;

import java.util.List;

/**
 * @author nobody
 * @date 2018/10/25
 */
public class ActivityGrandBean {

    public List<ListBean> list;

    public static class ListBean {
        /**
         * id : 838
         * name : 非诚勿扰
         * linkUrl : https://test-case.mengbitv.com/h5/payinfobox/index.html
         * jumpUrl : https://test-case.mengbitv.com/h5/payinfobox/index.html
         */

        public int id;
        public String name;
        public String linkUrl;
        public String jumpUrl;
    }
}
