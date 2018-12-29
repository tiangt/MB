package com.whzl.mengbi.model.entity;

import java.util.List;

/**
 * @author cliang
 * @date 2018.12.27
 */
public class HeadlineTopInfo extends ResponseInfo {
    private DataBean data;

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
             * programId : 100079
             * anchorAvatar : http://localtest-img.mengbitv.com/avatar/030/00/01/39_100x100.jpg?1545983901
             */

            private int programId;
            private String anchorAvatar;

            public int getProgramId() {
                return programId;
            }

            public void setProgramId(int programId) {
                this.programId = programId;
            }

            public String getAnchorAvatar() {
                return anchorAvatar;
            }

            public void setAnchorAvatar(String anchorAvatar) {
                this.anchorAvatar = anchorAvatar;
            }

        }
    }
}
