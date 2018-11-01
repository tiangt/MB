package com.whzl.mengbi.model.entity;

import java.util.List;

/**
 * @author nobody
 * @date 2018/10/23
 */
public class NewTaskBean {

    public List<ListBean> list;

    public static class ListBean {
        /**
         * awardId : 23070
         * awardName : 新人奖励注册
         * status : GRANT
         * awardSn :
         * needCompletion : 1
         * completion : 1
         */

        public int awardId;
        public String awardName;
        public String status;
        public String awardSn;
        public int needCompletion;
        public int completion;
    }
}
