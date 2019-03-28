package com.whzl.mengbi.model.entity;

import java.util.List;

/**
 * @author nobody
 * @date 2019/3/27
 */
public class AnchorWishRank {

    public List<ListBean> list;

    public static class ListBean {
        /**
         * userId : 30000844
         * nickName : 萌友30000844
         * score : 200
         */

        public int userId;
        public String nickName;
        public int score;
    }
}
