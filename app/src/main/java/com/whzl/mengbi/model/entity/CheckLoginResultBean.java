package com.whzl.mengbi.model.entity;

/**
 * @author shaw
 * @date 2018/7/17
 */
public class CheckLoginResultBean {

    /**
     * code : 200
     * msg : success
     * data : {"isLogin":true}
     */

    public int code;
    public String msg;
    public DataBean data;

    public static class DataBean {
        /**
         * isLogin : true
         */

        public boolean isLogin;
    }
}
