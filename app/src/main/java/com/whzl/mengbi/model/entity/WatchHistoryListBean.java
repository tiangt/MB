package com.whzl.mengbi.model.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shaw
 * @date 2018/7/30
 */
public class WatchHistoryListBean {

    public ArrayList<AnchorDetailBean> list;

    public class AnchorDetailBean {
        /**
         * programId : 100069
         * programName : 土豪30000219
         * status : F
         * roomUserCount : 0
         * cover : http://dev.img.mengbitv.com/default/000/00/00/44.jpg
         * anchorId : 30000219
         * anchorNickname : 土豪30000219
         * anchorLevelName : 红心1
         * anchorAvatar : http://test-img.mengbitv.com/avatar/030/00/01/39_100x100.jpg?1531813740
         * showStreamData : []
         */

        public int programId;
        public String programName;
        public String status;
        public int roomUserCount;
        public String cover;
        public int anchorId;
        public String anchorNickname;
        public String anchorLevelName;
        public String anchorAvatar;
        public List<?> showStreamData;
    }
}
