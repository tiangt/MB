package com.whzl.mengbi.model.entity;

import java.util.List;

/**
 * @author nobody
 * @date 2019/4/16
 */
public class SystemConfigBean {


    public List<ListBean> list;

    public static class ListBean {
        /**
         * paramName : 编辑昵称花费萌币
         * paramValue : 1000
         * unitType : 萌币
         */

        public String paramName;
        public String paramValue;
        public String unitType;
    }
}
