package com.whzl.mengbi.model.entity;

import java.util.List;

/**
 * @author nobody
 * @date 2018/10/24
 */
public class BillPayBean {

    /**
     * numberTotal : 783910
     * list : [{"goodsId":301,"goodsName":"vip","goodsNum":1,"coinNum":30000,"createTime":"2018-10-15 11:53:19","busiCode":205,"busiName":"购买物品"},{"goodsId":94337,"goodsName":"壁咚","goodsNum":1,"coinNum":52000,"createTime":"2018-10-11 14:03:41","busiCode":810,"busiName":"送礼"},{"goodsId":94454,"goodsName":"7891237","goodsNum":1,"coinNum":100060,"createTime":"2018-10-10 13:08:47","busiCode":205,"busiName":"购买物品"},{"goodsId":300,"goodsName":"守护","goodsNum":1,"coinNum":30000,"createTime":"2018-10-10 10:06:11","busiCode":205,"busiName":"购买物品"},{"goodsId":94307,"goodsName":"小黄瓜","goodsNum":520,"coinNum":52000,"createTime":"2018-10-09 11:02:08","busiCode":810,"busiName":"送礼"}]
     */

    public long numberTotal;
    public List<ListBean> list;

    public static class ListBean {
        /**
         * goodsId : 301
         * goodsName : vip
         * goodsNum : 1
         * coinNum : 30000
         * createTime : 2018-10-15 11:53:19
         * busiCode : 205
         * busiName : 购买物品
         */

        public int goodsId;
        public String goodsName;
        public int goodsNum;
        public long coinNum;
        public String createTime;
        public int busiCode;
        public String busiName;
    }
}
