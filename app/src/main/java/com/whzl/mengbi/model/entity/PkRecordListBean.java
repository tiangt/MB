package com.whzl.mengbi.model.entity;

import java.util.List;

/**
 * @author nobody
 * @date 2018/12/28
 */
public class PkRecordListBean {

    public List<ListBean> list;

    public static class ListBean {
        /**
         * nickname : 你一定要幸福哦哈嘿嗯
         * userId : 30000139
         * lastUpdateTime : 2018-11-23 09:44:18
         * anchorLevel : 50
         * result : F
         * date : 2018-12-21 14:08:18
         */

        public String nickname;
        public int userId;
        public String lastUpdateTime;
        public int anchorLevel;
        public String result;
        public String date;
    }
}
