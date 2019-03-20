package com.whzl.mengbi.model.entity;


import java.util.List;

/**
 * @author shaw
 */
public class AnchorInfo {


    /**
     * programId : 100079
     * programName : 听见下雨的声音
     * programType : 0
     * pubNotice : 在最美的时光遇见你~
     * status : ONLINE
     * programStatus : T
     * subscriptionNum : 26
     * anchor : {"id":30000139,"name":"路奇","city":"杭州市","province":"浙江省","lastUpdateTime":"2018-09-19 11:19:17","level":[{"levelType":"ANCHOR_LEVEL","levelValue":50,"levelName":"天使10","expList":[{"expType":"GIFT_EXP","expName":"付费礼物经验","sjExpvalue":4470327646,"bjExpValue":4470327646,"sjNeedExpValue":0,"bjNeedExpValue":0}]},{"levelType":"ROYAL_LEVEL","levelValue":7,"levelName":"王者","expList":[{"expType":"ROYAL_EXP","expName":"贵族经验","sjExpvalue":120020,"bjExpValue":100020,"sjNeedExpValue":0,"bjNeedExpValue":20000}]},{"levelType":"USER_LEVEL","levelValue":29,"levelName":"国师","expList":[{"expType":"USER_EXP","expName":"用户经验","sjExpvalue":129209201,"bjExpValue":129209201,"sjNeedExpValue":200000000,"bjNeedExpValue":0}]}],"anchorPlatformType":"ENT","introduce":"哈哈哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈哈哈哈啊哈哈哈","avatar":"https://test-img.mengbitv.com/avatar/030/00/01/39_100x100.jpg?1537327157"}
     * stream : {"height":600,"width":800,"streamAddress":{"rtmp":"rtmp://livedown.mengbitv.com/live/100079","flv":"http://livedown.mengbitv.com/live/100079.flv","hls":"http://livedown.mengbitv.com/live/100079/playlist.m3u8"},"liveType":"PC"}
     */

    public int programId;
    public String programName;
    public String programType;
    public String pubNotice;
    public String status;
    public String cover;
    public String avatar;
    public String programStatus;
    public int subscriptionNum;
    public AnchorBean anchor;
    public StreamBean stream;

    public static class AnchorBean {
        /**
         * id : 30000139
         * name : 路奇
         * city : 杭州市
         * province : 浙江省
         * lastUpdateTime : 2018-09-19 11:19:17
         * level : [{"levelType":"ANCHOR_LEVEL","levelValue":50,"levelName":"天使10","expList":[{"expType":"GIFT_EXP","expName":"付费礼物经验","sjExpvalue":4470327646,"bjExpValue":4470327646,"sjNeedExpValue":0,"bjNeedExpValue":0}]},{"levelType":"ROYAL_LEVEL","levelValue":7,"levelName":"王者","expList":[{"expType":"ROYAL_EXP","expName":"贵族经验","sjExpvalue":120020,"bjExpValue":100020,"sjNeedExpValue":0,"bjNeedExpValue":20000}]},{"levelType":"USER_LEVEL","levelValue":29,"levelName":"国师","expList":[{"expType":"USER_EXP","expName":"用户经验","sjExpvalue":129209201,"bjExpValue":129209201,"sjNeedExpValue":200000000,"bjNeedExpValue":0}]}]
         * anchorPlatformType : ENT
         * introduce : 哈哈哈哈哈哈哈哈哈哈哈哈哈啊哈哈哈哈哈哈哈啊哈哈哈
         * avatar : https://test-img.mengbitv.com/avatar/030/00/01/39_100x100.jpg?1537327157
         */

        public int id;
        public String name;
        public String city;
        public String province;
        public String lastUpdateTime;
        public String anchorPlatformType;
        public String introduce;
        public String avatar;
        public List<LevelBean> level;

        public static class LevelBean {
            /**
             * levelType : ANCHOR_LEVEL
             * levelValue : 50
             * levelName : 天使10
             * expList : [{"expType":"GIFT_EXP","expName":"付费礼物经验","sjExpvalue":4470327646,"bjExpValue":4470327646,"sjNeedExpValue":0,"bjNeedExpValue":0}]
             */

            public String levelType;
            public int levelValue;
            public String levelName;
            public List<ExpListBean> expList;

            public static class ExpListBean {
                /**
                 * expType : GIFT_EXP
                 * expName : 付费礼物经验
                 * sjExpvalue : 4470327646
                 * bjExpValue : 4470327646
                 * sjNeedExpValue : 0
                 * bjNeedExpValue : 0
                 */

                public String expType;
                public String expName;
                public long sjExpvalue;
                public long bjExpValue;
                public int sjNeedExpValue;
                public int bjNeedExpValue;
            }
        }
    }

    public static class StreamBean {
        /**
         * height : 600
         * width : 800
         * streamAddress : {"rtmp":"rtmp://livedown.mengbitv.com/live/100079","flv":"http://livedown.mengbitv.com/live/100079.flv","hls":"http://livedown.mengbitv.com/live/100079/playlist.m3u8"}
         * liveType : PC
         */

        public int height;
        public int width;
        public StreamAddressBean streamAddress;
        public String liveType;

        public static class StreamAddressBean {
            /**
             * rtmp : rtmp://livedown.mengbitv.com/live/100079
             * flv : http://livedown.mengbitv.com/live/100079.flv
             * hls : http://livedown.mengbitv.com/live/100079/playlist.m3u8
             */

            public String rtmp;
            public String flv;
            public String hls;
        }
    }
}
