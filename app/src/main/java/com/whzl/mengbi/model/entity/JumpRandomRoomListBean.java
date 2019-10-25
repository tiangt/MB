package com.whzl.mengbi.model.entity;

import java.util.List;

/**
 * @author nobody
 * @date 2018/10/24
 */
public class JumpRandomRoomListBean {


    public List<ListBean> list;

    public static class ListBean {
        /**
         * programId : 100170
         * programName : 萌友30000814
         * status : T
         * roomUserCount : 0
         * cover : http://localtest-img.mengbitv.com/default/000/00/00/00_280x280.jpg
         * anchorId : 30000814
         * anchorNickname : 萌友30000814
         * anchorLevelName : 天使1
         * anchorLevelValue : 41
         * isWeekFlag : F
         * isPopularityFlag : F
         * isPk : F
         * showStreamData : {"height":540,"width":960,"liveType":"PC","rtmp":"rtmp://livedown.mengbitv.com/live/100170","flv":"http://livedown.mengbitv.com/live/100170.flv","hls":"http://livedown.mengbitv.com/live/100170/playlist.m3u8"}
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
        public String isWeekFlag;
        public String isPopularityFlag;
        public String isPk;
        public ShowStreamDataBean showStreamData;

        public static class ShowStreamDataBean {
            /**
             * height : 540
             * width : 960
             * liveType : PC
             * rtmp : rtmp://livedown.mengbitv.com/live/100170
             * flv : http://livedown.mengbitv.com/live/100170.flv
             * hls : http://livedown.mengbitv.com/live/100170/playlist.m3u8
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
