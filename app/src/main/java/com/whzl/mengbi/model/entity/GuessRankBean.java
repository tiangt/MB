package com.whzl.mengbi.model.entity;

import java.util.List;

/**
 * @author nobody
 * @date 2019-06-14
 */
public class GuessRankBean {

    public List<ListBean> list;

    public static class ListBean {
        /**
         * userId : 30000962
         * nickName : 萌友30000962
         * lastUpdateTime : 2019-06-12 20:03:03
         * score : 10090000
         */

        public long userId;
        public String nickName;
        public String lastUpdateTime;
        public int score;
    }
}
