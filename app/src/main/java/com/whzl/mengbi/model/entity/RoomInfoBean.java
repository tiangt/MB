package com.whzl.mengbi.model.entity;

import java.util.List;

/**
 * @author shaw
 * @date 2018/7/10
 */
public class RoomInfoBean {

    /**
     * code : 200
     * msg : success
     * data : {"programId":100079,"programName":"土豪30000139","programType":"0","pubNotice":"在最美的时光遇见你~","status":"ONLINE","programStatus":"T","subscriptionNum":13,"anchor":{"id":30000139,"name":"哈哈哈哈哈哈哈哈和哈","city":"上海","province":"浙江省","lastUpdateTime":"2018-06-06 10:18:00","level":[{"levelType":"ANCHOR_LEVEL","levelValue":49,"levelName":"天使9","expList":[{"expType":"GIFT_EXP","expName":"付费礼物经验","sjExpvalue":2970534412,"bjExpValue":2970534412,"sjNeedExpValue":3000000000,"bjNeedExpValue":0}]},{"levelType":"ROYAL_LEVEL","levelValue":0,"levelName":"平民","expList":[{"expType":"ROYAL_EXP","expName":"贵族经验","sjExpvalue":0,"bjExpValue":0,"sjNeedExpValue":200,"bjNeedExpValue":0}]},{"levelType":"USER_LEVEL","levelValue":23,"levelName":"子爵","expList":[{"expType":"USER_EXP","expName":"用户经验","sjExpvalue":10022200,"bjExpValue":10022200,"sjNeedExpValue":15000000,"bjNeedExpValue":0}]}],"anchorPlatformType":"ENT","introduce":"php echo ？","avatar":"http://dev.img.mengbitv.com/avatar/030/00/01/39.jpg?1528251480"},"stream":{"height":600,"width":800,"streamAddress":{"rtmp":"rtmp://livedown.mengbitv.com/live/100079","flv":"http://livedown.mengbitv.com/live/100079.flv","hls":"http://livedown.mengbitv.com/live/100079/playlist.m3u8"},"liveType":"PC"}}
     */

    private int code;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * programId : 100079
         * programName : 土豪30000139
         * programType : 0
         * pubNotice : 在最美的时光遇见你~
         * status : ONLINE
         * programStatus : T
         * subscriptionNum : 13
         * anchor : {"id":30000139,"name":"哈哈哈哈哈哈哈哈和哈","city":"上海","province":"浙江省","lastUpdateTime":"2018-06-06 10:18:00","level":[{"levelType":"ANCHOR_LEVEL","levelValue":49,"levelName":"天使9","expList":[{"expType":"GIFT_EXP","expName":"付费礼物经验","sjExpvalue":2970534412,"bjExpValue":2970534412,"sjNeedExpValue":3000000000,"bjNeedExpValue":0}]},{"levelType":"ROYAL_LEVEL","levelValue":0,"levelName":"平民","expList":[{"expType":"ROYAL_EXP","expName":"贵族经验","sjExpvalue":0,"bjExpValue":0,"sjNeedExpValue":200,"bjNeedExpValue":0}]},{"levelType":"USER_LEVEL","levelValue":23,"levelName":"子爵","expList":[{"expType":"USER_EXP","expName":"用户经验","sjExpvalue":10022200,"bjExpValue":10022200,"sjNeedExpValue":15000000,"bjNeedExpValue":0}]}],"anchorPlatformType":"ENT","introduce":"php echo ？","avatar":"http://dev.img.mengbitv.com/avatar/030/00/01/39.jpg?1528251480"}
         * stream : {"height":600,"width":800,"streamAddress":{"rtmp":"rtmp://livedown.mengbitv.com/live/100079","flv":"http://livedown.mengbitv.com/live/100079.flv","hls":"http://livedown.mengbitv.com/live/100079/playlist.m3u8"},"liveType":"PC"}
         */

        private int programId;
        private String programName;
        private String programType;
        private String pubNotice;
        private String status;
        private String programStatus;
        private int subscriptionNum;
        private AnchorBean anchor;
        private StreamBean stream;

        public int getProgramId() {
            return programId;
        }

        public void setProgramId(int programId) {
            this.programId = programId;
        }

        public String getProgramName() {
            return programName;
        }

        public void setProgramName(String programName) {
            this.programName = programName;
        }

        public String getProgramType() {
            return programType;
        }

        public void setProgramType(String programType) {
            this.programType = programType;
        }

        public String getPubNotice() {
            return pubNotice;
        }

        public void setPubNotice(String pubNotice) {
            this.pubNotice = pubNotice;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getProgramStatus() {
            return programStatus;
        }

        public void setProgramStatus(String programStatus) {
            this.programStatus = programStatus;
        }

        public int getSubscriptionNum() {
            return subscriptionNum;
        }

        public void setSubscriptionNum(int subscriptionNum) {
            this.subscriptionNum = subscriptionNum;
        }

        public AnchorBean getAnchor() {
            return anchor;
        }

        public void setAnchor(AnchorBean anchor) {
            this.anchor = anchor;
        }

        public StreamBean getStream() {
            return stream;
        }

        public void setStream(StreamBean stream) {
            this.stream = stream;
        }

        public static class AnchorBean {
            /**
             * id : 30000139
             * name : 哈哈哈哈哈哈哈哈和哈
             * city : 上海
             * province : 浙江省
             * lastUpdateTime : 2018-06-06 10:18:00
             * level : [{"levelType":"ANCHOR_LEVEL","levelValue":49,"levelName":"天使9","expList":[{"expType":"GIFT_EXP","expName":"付费礼物经验","sjExpvalue":2970534412,"bjExpValue":2970534412,"sjNeedExpValue":3000000000,"bjNeedExpValue":0}]},{"levelType":"ROYAL_LEVEL","levelValue":0,"levelName":"平民","expList":[{"expType":"ROYAL_EXP","expName":"贵族经验","sjExpvalue":0,"bjExpValue":0,"sjNeedExpValue":200,"bjNeedExpValue":0}]},{"levelType":"USER_LEVEL","levelValue":23,"levelName":"子爵","expList":[{"expType":"USER_EXP","expName":"用户经验","sjExpvalue":10022200,"bjExpValue":10022200,"sjNeedExpValue":15000000,"bjNeedExpValue":0}]}]
             * anchorPlatformType : ENT
             * introduce : php echo ？
             * avatar : http://dev.img.mengbitv.com/avatar/030/00/01/39.jpg?1528251480
             */

            private int id;
            private String name;
            private String city;
            private String province;
            private String lastUpdateTime;
            private String anchorPlatformType;
            private String introduce;
            private String avatar;
            private List<LevelBean> level;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getProvince() {
                return province;
            }

            public void setProvince(String province) {
                this.province = province;
            }

            public String getLastUpdateTime() {
                return lastUpdateTime;
            }

            public void setLastUpdateTime(String lastUpdateTime) {
                this.lastUpdateTime = lastUpdateTime;
            }

            public String getAnchorPlatformType() {
                return anchorPlatformType;
            }

            public void setAnchorPlatformType(String anchorPlatformType) {
                this.anchorPlatformType = anchorPlatformType;
            }

            public String getIntroduce() {
                return introduce;
            }

            public void setIntroduce(String introduce) {
                this.introduce = introduce;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public List<LevelBean> getLevel() {
                return level;
            }

            public void setLevel(List<LevelBean> level) {
                this.level = level;
            }

            public static class LevelBean {
                /**
                 * levelType : ANCHOR_LEVEL
                 * levelValue : 49
                 * levelName : 天使9
                 * expList : [{"expType":"GIFT_EXP","expName":"付费礼物经验","sjExpvalue":2970534412,"bjExpValue":2970534412,"sjNeedExpValue":3000000000,"bjNeedExpValue":0}]
                 */

                private String levelType;
                private int levelValue;
                private String levelName;
                private List<ExpListBean> expList;

                public String getLevelType() {
                    return levelType;
                }

                public void setLevelType(String levelType) {
                    this.levelType = levelType;
                }

                public int getLevelValue() {
                    return levelValue;
                }

                public void setLevelValue(int levelValue) {
                    this.levelValue = levelValue;
                }

                public String getLevelName() {
                    return levelName;
                }

                public void setLevelName(String levelName) {
                    this.levelName = levelName;
                }

                public List<ExpListBean> getExpList() {
                    return expList;
                }

                public void setExpList(List<ExpListBean> expList) {
                    this.expList = expList;
                }

                public static class ExpListBean {
                    /**
                     * expType : GIFT_EXP
                     * expName : 付费礼物经验
                     * sjExpvalue : 2970534412
                     * bjExpValue : 2970534412
                     * sjNeedExpValue : 3000000000
                     * bjNeedExpValue : 0
                     */

                    private String expType;
                    private String expName;
                    private long sjExpvalue;
                    private long bjExpValue;
                    private long sjNeedExpValue;
                    private int bjNeedExpValue;

                    public String getExpType() {
                        return expType;
                    }

                    public void setExpType(String expType) {
                        this.expType = expType;
                    }

                    public String getExpName() {
                        return expName;
                    }

                    public void setExpName(String expName) {
                        this.expName = expName;
                    }

                    public long getSjExpvalue() {
                        return sjExpvalue;
                    }

                    public void setSjExpvalue(long sjExpvalue) {
                        this.sjExpvalue = sjExpvalue;
                    }

                    public long getBjExpValue() {
                        return bjExpValue;
                    }

                    public void setBjExpValue(long bjExpValue) {
                        this.bjExpValue = bjExpValue;
                    }

                    public long getSjNeedExpValue() {
                        return sjNeedExpValue;
                    }

                    public void setSjNeedExpValue(long sjNeedExpValue) {
                        this.sjNeedExpValue = sjNeedExpValue;
                    }

                    public int getBjNeedExpValue() {
                        return bjNeedExpValue;
                    }

                    public void setBjNeedExpValue(int bjNeedExpValue) {
                        this.bjNeedExpValue = bjNeedExpValue;
                    }
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

            private int height;
            private int width;
            private StreamAddressBean streamAddress;
            private String liveType;

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

            public StreamAddressBean getStreamAddress() {
                return streamAddress;
            }

            public void setStreamAddress(StreamAddressBean streamAddress) {
                this.streamAddress = streamAddress;
            }

            public String getLiveType() {
                return liveType;
            }

            public void setLiveType(String liveType) {
                this.liveType = liveType;
            }

            public static class StreamAddressBean {
                /**
                 * rtmp : rtmp://livedown.mengbitv.com/live/100079
                 * flv : http://livedown.mengbitv.com/live/100079.flv
                 * hls : http://livedown.mengbitv.com/live/100079/playlist.m3u8
                 */

                private String rtmp;
                private String flv;
                private String hls;

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
