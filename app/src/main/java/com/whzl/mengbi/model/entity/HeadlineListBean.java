package com.whzl.mengbi.model.entity;

import java.util.List;

/**
 * @author cliang
 * @date 2018.12.26
 */
public class HeadlineListBean {

    /**
     * code : 200
     * msg : success
     * data : {"list":[{"programId":100148,"programName":"马上开播了2sdfd","status":"T","roomUserCount":3,"cover":"http://localtest-img.mengbitv.com/default/000/00/03/54_400x400.jpg","anchorId":30000607,"anchorNickname":"萌友30000607","anchorLevelName":"银冠8","anchorLevelValue":23,"anchorAvatar":"http://localtest-img.mengbitv.com/avatar/030/00/06/07_100x100.jpg?1545797189","showStreamData":{"height":600,"width":800,"liveType":"PC","rtmp":"rtmp://livedown.mengbitv.com/live/100148","flv":"http://livedown.mengbitv.com/live/100148.flv","hls":"http://livedown.mengbitv.com/live/100148/playlist.m3u8"},"lastShowBeginTime":"2018-11-12 11:12:29","isPk":"F","rank":1,"score":40000},{"programId":100001,"programName":"土豪30000012","status":"T","roomUserCount":0,"cover":"http://localtest-img.mengbitv.com/default/000/00/00/23_400x400.jpg","anchorId":30000012,"anchorNickname":"修改昵称","anchorLevelName":"皇冠20","anchorLevelValue":35,"anchorAvatar":"http://localtest-img.mengbitv.com/avatar/030/00/00/12_100x100.jpg?1545797189","showStreamData":{"height":600,"width":800,"liveType":"PC","rtmp":"rtmp://livedown.mengbitv.com/live/100001","flv":"http://livedown.mengbitv.com/live/100001.flv","hls":"http://livedown.mengbitv.com/live/100001/playlist.m3u8"},"lastShowBeginTime":"2018-11-12 11:15:00","isPk":"F","rank":2,"score":20000}],"leftTime":0}
     */

    public int code;
    public String msg;
    public DataBean data;

    public static class DataBean {
        /**
         * list : [{"programId":100148,"programName":"马上开播了2sdfd","status":"T","roomUserCount":3,"cover":"http://localtest-img.mengbitv.com/default/000/00/03/54_400x400.jpg","anchorId":30000607,"anchorNickname":"萌友30000607","anchorLevelName":"银冠8","anchorLevelValue":23,"anchorAvatar":"http://localtest-img.mengbitv.com/avatar/030/00/06/07_100x100.jpg?1545797189","showStreamData":{"height":600,"width":800,"liveType":"PC","rtmp":"rtmp://livedown.mengbitv.com/live/100148","flv":"http://livedown.mengbitv.com/live/100148.flv","hls":"http://livedown.mengbitv.com/live/100148/playlist.m3u8"},"lastShowBeginTime":"2018-11-12 11:12:29","isPk":"F","rank":1,"score":40000},{"programId":100001,"programName":"土豪30000012","status":"T","roomUserCount":0,"cover":"http://localtest-img.mengbitv.com/default/000/00/00/23_400x400.jpg","anchorId":30000012,"anchorNickname":"修改昵称","anchorLevelName":"皇冠20","anchorLevelValue":35,"anchorAvatar":"http://localtest-img.mengbitv.com/avatar/030/00/00/12_100x100.jpg?1545797189","showStreamData":{"height":600,"width":800,"liveType":"PC","rtmp":"rtmp://livedown.mengbitv.com/live/100001","flv":"http://livedown.mengbitv.com/live/100001.flv","hls":"http://livedown.mengbitv.com/live/100001/playlist.m3u8"},"lastShowBeginTime":"2018-11-12 11:15:00","isPk":"F","rank":2,"score":20000}]
         * leftTime : 0
         */

        public int leftTime;
        public List<ListBean> list;

        public static class ListBean {
            /**
             * programId : 100148
             * programName : 马上开播了2sdfd
             * status : T
             * roomUserCount : 3
             * cover : http://localtest-img.mengbitv.com/default/000/00/03/54_400x400.jpg
             * anchorId : 30000607
             * anchorNickname : 萌友30000607
             * anchorLevelName : 银冠8
             * anchorLevelValue : 23
             * anchorAvatar : http://localtest-img.mengbitv.com/avatar/030/00/06/07_100x100.jpg?1545797189
             * showStreamData : {"height":600,"width":800,"liveType":"PC","rtmp":"rtmp://livedown.mengbitv.com/live/100148","flv":"http://livedown.mengbitv.com/live/100148.flv","hls":"http://livedown.mengbitv.com/live/100148/playlist.m3u8"}
             * lastShowBeginTime : 2018-11-12 11:12:29
             * isPk : F
             * rank : 1
             * score : 40000
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
            public int rank;
            public int score;

            public static class ShowStreamDataBean {
                /**
                 * height : 600
                 * width : 800
                 * liveType : PC
                 * rtmp : rtmp://livedown.mengbitv.com/live/100148
                 * flv : http://livedown.mengbitv.com/live/100148.flv
                 * hls : http://livedown.mengbitv.com/live/100148/playlist.m3u8
                 */

                private int height;
                private int width;
                private String liveType;
                private String rtmp;
                private String flv;
                private String hls;

                public int getHeight() {
                    return height;
                }

                public void setHeight(int height) {
                    this.height = height;
                }

                public int getWidth() {
                    return width;
                }

                public void setWidth(int width) {
                    this.width = width;
                }

                public String getLiveType() {
                    return liveType;
                }

                public void setLiveType(String liveType) {
                    this.liveType = liveType;
                }

                public String getRtmp() {
                    return rtmp;
                }

                public void setRtmp(String rtmp) {
                    this.rtmp = rtmp;
                }

                public String getFlv() {
                    return flv;
                }

                public void setFlv(String flv) {
                    this.flv = flv;
                }

                public String getHls() {
                    return hls;
                }

                public void setHls(String hls) {
                    this.hls = hls;
                }
            }
        }
    }
}
