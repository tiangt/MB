package com.whzl.mengbi.model.entity;

import java.util.ArrayList;

/**
 * @author shaw
 * @date 2018/7/20
 */
public class AnchorFollowedDataBean {


    public int code;
    public String msg;
    public DataBean data;

    public class DataBean {
        public int mark;
        public ArrayList<AnchorInfoBean> list;
    }

    public class ShowStreamDataBean {
        public int height;
        public int width;
        public String liveType;
        public String rtmp;
        public String flv;
        public String hls;
    }

    public class AnchorInfoBean {
        public int programId;
        public String programName;
        public String status;
        public int roomUserCount;
        public String cover;
        public int anchorId;
        public String anchorNickname;
        public int anchorLevelValue;
        public ShowStreamDataBean showStreamData;
        public String avatar;
        public String lastShowBeginTime;
    }
}
