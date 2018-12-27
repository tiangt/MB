package com.whzl.mengbi.model.entity;

import java.util.List;

/**
 * @author cliang
 * @date 2018.12.27
 */
public class HeadlineTopInfo extends ResponseInfo{

    /**
     * data : {"list":[{"programId":100069,"programName":"土豪30000219","status":"F","roomUserCount":0,"cover":"http://dev.img.mengbitv.com/default/000/00/00/44.jpg","anchorId":30000219,"anchorNickname":"土豪30000219","anchorLevelName":"蓝钻1","anchorLevelValue":6,"anchorAvatar":"http://test-img.mengbitv.com/avatar/030/00/01/39_100x100.jpg?1531813740","showStreamData":[],"avatar":"http://dev.img.mengbitv.com/avatar/030/00/02/19.jpg?1525313863"}]}
     */

    public DataBean data;
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
            /**
             * programId : 100069
             * programName : 土豪30000219
             * status : F
             * roomUserCount : 0
             * cover : http://dev.img.mengbitv.com/default/000/00/00/44.jpg
             * anchorId : 30000219
             * anchorNickname : 土豪30000219
             * anchorLevelName : 蓝钻1
             * anchorLevelValue : 6
             * anchorAvatar : http://test-img.mengbitv.com/avatar/030/00/01/39_100x100.jpg?1531813740
             * showStreamData : []
             * avatar : http://dev.img.mengbitv.com/avatar/030/00/02/19.jpg?1525313863
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
            public String avatar;
            public List<?> showStreamData;

            public List<?> getShowStreamData() {
                return showStreamData;
            }

            public void setShowStreamData(List<?> showStreamData) {
                this.showStreamData = showStreamData;
            }
        }
    }
}
