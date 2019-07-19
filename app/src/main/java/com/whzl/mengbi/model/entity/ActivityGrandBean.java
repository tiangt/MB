package com.whzl.mengbi.model.entity;

import java.util.List;

/**
 * @author nobody
 * @date 2018/10/25
 */
public class ActivityGrandBean {

    /**
     * code : 200
     * data : {"list":[{"id":857,"name":"清凉一夏","linkUrl":"https://test-case.mengbitv.com/h5/activity0715/approom.html?today=2019-07-20","jumpUrl":"https://test-case.mengbitv.com/h5/activity0715/index.html?today=2019-07-21"}]}
     * msg : success
     * success : false
     */

    public int code;
    public DataBean data;
    public String msg;
    public boolean success;

    public static class DataBean {
        public List<ListBean> list;

        public static class ListBean {
            /**
             * id : 857
             * name : 清凉一夏
             * linkUrl : https://test-case.mengbitv.com/h5/activity0715/approom.html?today=2019-07-20
             * jumpUrl : https://test-case.mengbitv.com/h5/activity0715/index.html?today=2019-07-21
             */

            public int id;
            public String name;
            public String linkUrl;
            public String jumpUrl;
        }
    }
}
