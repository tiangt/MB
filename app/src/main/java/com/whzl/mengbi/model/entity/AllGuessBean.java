package com.whzl.mengbi.model.entity;

import java.util.List;

/**
 * @author nobody
 * @date 2019-06-12
 */
public class AllGuessBean {

    public List<ListBean> list;

    public static class ListBean {
        /**
         * programId : 100164
         * programName : 萌友30000804
         * status : T
         * roomUserCount : 1
         * cover : https://test-img.mengbitv.com/default/000/00/00/00_400x400.jpg
         * anchorId : 30000804
         * anchorNickname : 胡哈30000804
         * anchorLevelName : 天使1
         * anchorLevelValue : 41
         * anchorAvatar : https://test-img.mengbitv.com/avatar/030/00/08/04_100x100.jpg?1560310873
         * showStreamData : {"height":540,"width":960,"liveType":"PC","rtmp":"rtmp://livedown.mengbitv.com/live/100164","flv":"http://livedown.mengbitv.com/live/100164.flv","hls":"http://livedown.mengbitv.com/live/100164/playlist.m3u8"}
         * lastShowBeginTime : 2019-04-28 15:02:30
         * isPk : F
         * isWeekStar : F
         * isPopularity : F
         * roomUserNum : 1
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
        public ShowStreamDataBean showStreamData;
        public String lastShowBeginTime;
        public String isPk;
        public String isWeekStar;
        public String isPopularity;
        public int roomUserNum;

        public static class ShowStreamDataBean {
            /**
             * height : 540
             * width : 960
             * liveType : PC
             * rtmp : rtmp://livedown.mengbitv.com/live/100164
             * flv : http://livedown.mengbitv.com/live/100164.flv
             * hls : http://livedown.mengbitv.com/live/100164/playlist.m3u8
             */

            public int height;
            public int width;
            public String liveType;
            public String rtmp;
            public String flv;
            public String hls;
        }
    }
}
