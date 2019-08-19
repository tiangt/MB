package com.whzl.mengbi.model.entity;

import java.util.ArrayList;

/**
 * @author nobody
 * @date 2019-08-12
 */
public class AnchorTopBean {

    public ArrayList<ListBean> list;
    /**
     * userRankInfo : {"rankIndex":263,"preCharmGap":0}
     */

    public UserRankInfoBean userRankInfo;

    public static class ListBean {
        /**
         * programId : 100122
         * programName : 土豪30000147
         * status : T
         * roomUserCount : 0
         * cover : http://localtest-img.mengbitv.com/default/000/00/00/00_400x400.jpg
         * anchorId : 30000147
         * anchorNickname : 30000147
         * anchorLevelName : 银冠5
         * anchorLevelValue : 20
         * anchorAvatar : http://localtest-img.mengbitv.com/avatar/030/00/01/47_100x100.jpg?1565590249
         * showStreamData : {"height":540,"width":960,"liveType":"PC","rtmp":"rtmp://livedown.mengbitv.com/live/100122","flv":"http://livedown.mengbitv.com/live/100122.flv","hls":"http://livedown.mengbitv.com/live/100122/playlist.m3u8"}
         */

        public int programId;
        public String programName;
        public String status;
        public String lastUpdateTime;
        public int roomUserCount;
        public String cover;
        public long anchorId;
        public long userId;
        public String nickname;
        public String anchorLevelName;
        public int anchorLevel;
        public int userLevel;
        public long upAndDownNumber;

    }


    public static class UserRankInfoBean {
        /**
         * rankIndex : 263
         * preCharmGap : 0
         */

        public long rankIndex;
        public long preCharmGap;
        public String nickname;
        public int level;
    }
}
