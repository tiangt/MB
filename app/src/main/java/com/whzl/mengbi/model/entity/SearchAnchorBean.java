package com.whzl.mengbi.model.entity;

import java.util.List;

/**
 * @author nobody
 * @date 2018/9/29
 */
public class SearchAnchorBean {


    public List<ListBean> list;

    public static class ListBean {
        /**
         * programId : 100079
         * programName : 听见下雨的声音
         * status : T
         * roomUserCount : 0
         * cover : https://test-img.mengbitv.com/default/000/00/00/00_400x400.jpg
         * anchorId : 30000139
         * anchorNickname : 路奇
         * anchorLevelName : 天使10
         * anchorLevelValue : 50
         * anchorAvatar : https://test-img.mengbitv.com/avatar/030/00/01/39_100x100.jpg?1538213566
         * showStreamData : {"height":600,"width":800,"liveType":"PC","rtmp":"rtmp://livedown.mengbitv.com/live/100079","flv":"http://livedown.mengbitv.com/live/100079.flv","hls":"http://livedown.mengbitv.com/live/100079/playlist.m3u8"}
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

        public static class ShowStreamDataBean {
            /**
             * height : 600
             * width : 800
             * liveType : PC
             * rtmp : rtmp://livedown.mengbitv.com/live/100079
             * flv : http://livedown.mengbitv.com/live/100079.flv
             * hls : http://livedown.mengbitv.com/live/100079/playlist.m3u8
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
