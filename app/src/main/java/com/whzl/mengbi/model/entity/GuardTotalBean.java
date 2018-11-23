package com.whzl.mengbi.model.entity;

/**
 * @author cliang
 * @date 2018.11.7
 */
public class GuardTotalBean {
    /**
     * code : 200
     * msg : success
     * data : {"guardTotal":5,"onLineTotal":0,"offLineTotal":5}
     */

    public int code;
    public String msg;
    public DataBean data;

    public class DataBean {
        /**
         * guardTotal : 5
         * onLineTotal : 0
         * offLineTotal : 5
         */
        public int guardTotal;
        public int onLineTotal;
        public int offLineTotal;

    }
}
