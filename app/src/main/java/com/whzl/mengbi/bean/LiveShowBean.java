package com.whzl.mengbi.bean;

import java.util.List;

public class LiveShowBean {

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
        private List<ListBean> list;

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            private String status;
            private int roomUserCount;
            private String cover;
            private int anchorId;
            private String anchorNickname;
            private String anchorLevelName;
            private StreamBean stream;

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public int getRoomUserCount() {
                return roomUserCount;
            }

            public void setRoomUserCount(int roomUserCount) {
                this.roomUserCount = roomUserCount;
            }

            public String getCover() {
                return cover;
            }

            public void setCover(String cover) {
                this.cover = cover;
            }

            public int getAnchorId() {
                return anchorId;
            }

            public void setAnchorId(int anchorId) {
                this.anchorId = anchorId;
            }

            public String getAnchorNickname() {
                return anchorNickname;
            }

            public void setAnchorNickname(String anchorNickname) {
                this.anchorNickname = anchorNickname;
            }

            public String getAnchorLevelName() {
                return anchorLevelName;
            }

            public void setAnchorLevelName(String anchorLevelName) {
                this.anchorLevelName = anchorLevelName;
            }

            public StreamBean getStream() {
                return stream;
            }

            public void setStream(StreamBean stream) {
                this.stream = stream;
            }

            public static class StreamBean {
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
    @Override
    public String toString() {
        return "LiveShowBean{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
