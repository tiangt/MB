package com.whzl.mengbi.model.entity;

import java.util.List;

/**
 * @author nobody
 * @date 2019/4/16
 */
public class UpdownAnchorBean {

    public List<ListBean> list;

    public static class ListBean {
        /**
         * programId : 100165
         * coverUrl : http://localtest-img.mengbitv.com/default/000/00/00/00_400x400.jpg
         */

        public int programId;
        public String coverUrl;

        public ListBean(int programId, String coverUrl) {
            this.programId = programId;
            this.coverUrl = coverUrl;
        }
    }
}
