package com.whzl.mengbi.model.entity;

import java.util.List;

/**
 * @author cliang
 * @date 2018.12.27
 */
public class WeekStarGiftInfo extends ResponseInfo {

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
             * giftType : LUCK520
             * goodsName : 幸运520
             * goodsPic : https://localtest-img.mengbitv.com/app-img/week-star/37060.jpg
             */
            public String giftType;
            public String goodsName;
            public String goodsPic;
            public int anchorRankId;
            public int userRankId;
            public int goodsId;
        }
    }
}
