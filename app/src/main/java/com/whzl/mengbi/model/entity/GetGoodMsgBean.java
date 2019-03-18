package com.whzl.mengbi.model.entity;

import java.util.List;

/**
 * @author nobody
 * @date 2019/3/18
 */
public class GetGoodMsgBean {

    public List<ListBean> list;

    public static class ListBean {
        /**
         * messageId : 1
         * userId : 30000803
         * messageType : GOODS_TYPE
         * goodsId : 301
         * goodsName : VIP
         * surplusDay : 1
         * createTime : 2019-02-25 00:00:00
         * isRead : F
         * readTime : null
         * delFlag : F
         */

        public int messageId;
        public int userId;
        public String messageType;
        public int bindProgramId;
        public int goodsId;
        public String goodsName;
        public String goodsType;
        public int surplusDay;
        public String createTime;
        public String isRead;
        public Object readTime;
        public String delFlag;
    }
}
