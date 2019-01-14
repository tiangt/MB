package com.whzl.mengbi.model.entity;

import java.util.List;

/**
 * @author cliang
 * @date 2019.1.11
 */
public class CompositionListInfo extends ResponseInfo {

    public DataBean data;
    public static class DataBean {
        public List<ListBean> list;
        public static class ListBean {
            /**
             * compositionId : 10220
             * compositionName : 双旦超级跑车合成
             * num : null
             * expDate : null
             * everyUserNum : null
             * detailDtos : [{"compositionId":10220,"goodsId":94565,"num":5,"isUniversal":"F","goodsName":"超级跑车碎片①","picId":480,"goodsNum":0},{"compositionId":10220,"goodsId":94566,"num":5,"isUniversal":"F","goodsName":"超级跑车碎片","picId":481,"goodsNum":0},{"compositionId":10220,"goodsId":94567,"num":5,"isUniversal":"F","goodsName":"超级跑车碎片③","picId":482,"goodsNum":0}]
             * goodsDetails : [{"goodsName":"超级跑车","num":1,"type":"GIFT","effDays":null,"picId":373}]
             * isComposition : 0
             */

            public int compositionId;
            public String compositionName;
            public Object num;
            public Object expDate;
            public Object everyUserNum;
            public int isComposition;
            public List<DetailDtosBean> detailDtos;
            public List<GoodsDetailsBean> goodsDetails;

            public static class DetailDtosBean {
                /**
                 * compositionId : 10220
                 * goodsId : 94565
                 * num : 5
                 * isUniversal : F
                 * goodsName : 超级跑车碎片①
                 * picId : 480
                 * goodsNum : 0
                 */

                public int compositionId;
                public int goodsId;
                public int num;
                public String isUniversal;
                public String goodsName;
                public int picId;
                public int goodsNum;
            }

            public static class GoodsDetailsBean {
                /**
                 * goodsName : 超级跑车
                 * num : 1
                 * type : GIFT
                 * effDays : null
                 * picId : 373
                 */

                public String goodsName;
                public int num;
                public String type;
                public Object effDays;
                public int picId;
            }
        }
    }
}
