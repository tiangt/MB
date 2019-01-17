package com.whzl.mengbi.model.entity;

import java.util.List;

/**
 * @author cliang
 * @date 2019.1.17
 */
public class TrendingAnchorInfo extends ResponseInfo {

    public DataBean data;

    public static class DataBean {

        public List<ListBean> list;
        public static class ListBean {
            /**
             * programId : 100174
             * programName : 萌友30000809
             * status : T
             * roomUserCount : 10
             * cover : http://localtest-img.mengbitv.com/default/000/00/00/00_400x400.jpg
             * anchorId : 30000809
             * anchorNickname : 萌友30000809
             * anchorLevelName : 银冠8
             * anchorLevelValue : 23
             * anchorAvatar : http://localtest-img.mengbitv.com/avatar/030/00/08/09_100x100.jpg?1547689216
             * lastShowBeginTime : 2019-01-05 19:50:16
             * isPk : F
             * isWeekStar : F
             * isPopularity : F
             */

            public int programId;
            public String programName;
            public String status;
            public int roomUserCount;
            public String cover;
            public int anchorId;
            public String anchorNickname;
            public String anchorLevelName;
            public int anchorLevelValue;
            public String anchorAvatar;
            public String lastShowBeginTime;
            public String isPk;
            public String isWeekStar;
            public String isPopularity;
        }
    }
}
