package com.whzl.mengbi.model.entity;

import java.util.List;

/**
 * @author nobody
 * @date 2018/12/5
 */
public class DailyTaskBean {

    /**
     * login : {"awardSn":158840,"awardId":23160,"status":"UNGRANT"}
     * chat : {"awardSn":0,"awardId":23161,"status":"INACTIVE"}
     * watch : [{"awardSn":158842,"awardId":9,"status":"UNGRANT"},{"awardSn":0,"awardId":10,"status":"UNAWARD"},{"awardSn":0,"awardId":11,"status":"UNAWARD"},{"awardSn":0,"awardId":12,"status":"UNAWARD"},{"awardSn":0,"awardId":13,"status":"UNAWARD"}]
     */

    public LoginBean login;
    public LoginBean chat;
    public List<LoginBean> watch;

    public static class LoginBean {
        /**
         * awardSn : 158840
         * awardId : 23160
         * status : UNGRANT
         */

        public long awardSn;
        public int awardId;
        public String status;
    }
}
