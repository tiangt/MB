package com.whzl.mengbi.model.entity;

import java.util.List;

/**
 * @author cliang
 * @date 2019.1.11
 */
public class MyChipListInfo extends ResponseInfo {

    public DataBean data;
    public static class DataBean {

        public List<ListBean> list;
        public static class ListBean {
            /**
             * goodsSn : 94593
             * userId : 30000092
             * goodsId : 94565
             * goodsType : NORMAL_DEBRIS
             * prettyNum : null
             * goodsSum : 1
             * expDate : 2019-01-19 11:03:48
             * surplusDay : null
             * equipDay : null
             * isEquip : F
             * feeTime : null
             * bindProgramId : null
             * identifyCode : null
             * goodsColor : null
             * goods : {"goodsId":94565,"groupId":0,"goodsName":"超级跑车碎片①","goodsEngName":"shuangdanchaojipaochesuipian1","sortNum":null,"picId":480,"goodsType":"NORMAL_DEBRIS","maxNum":-1,"autoGetUserRule":null,"soldNum":0,"shopStoreNum":-1,"shopSaleNum":0,"createUser":30000003,"createTime":"2018-12-12 09:52:18","hasEquipAward":"F","userRule":null,"remark":"","status":"ACTIVE"}
             */

            public int goodsSn;
            public int userId;
            public int goodsId;
            public String goodsType;
            public Object prettyNum;
            public int goodsSum;
            public String expDate;
            public Object surplusDay;
            public Object equipDay;
            public String isEquip;
            public Object feeTime;
            public Object bindProgramId;
            public Object identifyCode;
            public Object goodsColor;
            public GoodsBean goods;

            public static class GoodsBean {
                /**
                 * goodsId : 94565
                 * groupId : 0
                 * goodsName : 超级跑车碎片①
                 * goodsEngName : shuangdanchaojipaochesuipian1
                 * sortNum : null
                 * picId : 480
                 * goodsType : NORMAL_DEBRIS
                 * maxNum : -1
                 * autoGetUserRule : null
                 * soldNum : 0
                 * shopStoreNum : -1
                 * shopSaleNum : 0
                 * createUser : 30000003
                 * createTime : 2018-12-12 09:52:18
                 * hasEquipAward : F
                 * userRule : null
                 * remark :
                 * status : ACTIVE
                 */

                public int goodsId;
                public int groupId;
                public String goodsName;
                public String goodsEngName;
                public Object sortNum;
                public int picId;
                public String goodsType;
                public int maxNum;
                public Object autoGetUserRule;
                public int soldNum;
                public int shopStoreNum;
                public int shopSaleNum;
                public int createUser;
                public String createTime;
                public String hasEquipAward;
                public Object userRule;
                public String remark;
                public String status;
            }
        }
    }
}
