package com.whzl.mengbi.bean;

import java.util.List;

public class ResultBean {

    /**
     * 状态码
     */
    private int code;
    /**
     * 状态值
     */
    private String msg;
    /**
     * 返回数据
     */
    private List<LiveBean> data;


    public class LiveBean{
        /**
         * 是否开播
         * T->开播
         * F->没有开播
         */
        private String status;
        /**
         * 直播间用户数
         */
        private int roomUserCount;
        /**
         * 主播封面
         */
        private String cover;
        /**
         * 主播id
         */
        private int anchorId;
        /**
         * 主播昵称（
         */
        private String anchorNickname;
        /**
         * 主播等级值
         */
        private int anchorLevelName;
        /**
         * 主播流
         * rtmp
         * flv
         * hls
         */
        private String Stream;

        private List<ResultBean> mList;

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

        public int getAnchorLevelName() {
            return anchorLevelName;
        }

        public void setAnchorLevelName(int anchorLevelName) {
            this.anchorLevelName = anchorLevelName;
        }

        public String getStream() {
            return Stream;
        }

        public void setStream(String stream) {
            Stream = stream;
        }
    }

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

    public List<LiveBean> getData() {
        return data;
    }

    public void setData(List<LiveBean> data) {
        this.data = data;
    }
}
