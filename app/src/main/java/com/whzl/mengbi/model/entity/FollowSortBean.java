package com.whzl.mengbi.model.entity;

import java.util.List;

/**
 * @author nobody
 * @date 2018/12/20
 */
public class FollowSortBean {


    public List<ListBean> list;

    public static class ListBean {
        /**
         * programId : 100001
         * programName : 土豪30000012
         * status : T
         * roomUserCount : 0
         * cover : https://test-img.mengbitv.com/default/000/00/00/23_400x400.jpg
         * anchorId : 30000012
         * anchorNickname : 修改昵称
         * anchorLevelName : 皇冠20
         * anchorLevelValue : 35
         * anchorAvatar : https://test-img.mengbitv.com/avatar/030/00/00/12_100x100.jpg?1545275720
         * showStreamData : {"height":600,"width":800,"liveType":"PC","rtmp":"rtmp://livedown.mengbitv.com/live/100001","flv":"http://livedown.mengbitv.com/live/100001.flv","hls":"http://livedown.mengbitv.com/live/100001/playlist.m3u8"}
         * lastShowBeginTime : 2018-11-12 11:15:00
         * isPk : F
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

        public static class ShowStreamDataBean {
            /**
             * height : 600
             * width : 800
             * liveType : PC
             * rtmp : rtmp://livedown.mengbitv.com/live/100001
             * flv : http://livedown.mengbitv.com/live/100001.flv
             * hls : http://livedown.mengbitv.com/live/100001/playlist.m3u8
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
