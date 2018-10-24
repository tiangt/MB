package com.whzl.mengbi.model.entity;

import java.util.List;

/**
 * @author nobody
 * @date 2018/10/24
 */
public class BillGiftBean {

    /**
     * numberTotal : 123800
     * list : [{"receiveUseId":30000012,"receiveNickname":"修改昵称","giftName":"壁咚","giftNum":1,"coinNum":52000,"createTime":"2018-10-11 14:03:41"},{"receiveUseId":30000012,"receiveNickname":"修改昵称","giftName":"小黄瓜","giftNum":520,"coinNum":52000,"createTime":"2018-10-09 11:02:08"}]
     */

    public long numberTotal;
    public List<ListBean> list;

    public static class ListBean {
        /**
         * receiveUseId : 30000012
         * receiveNickname : 修改昵称
         * giftName : 壁咚
         * giftNum : 1
         * coinNum : 52000
         * createTime : 2018-10-11 14:03:41
         */

        public int receiveUseId;
        public String receiveNickname;
        public String giftName;
        public int giftNum;
        public long coinNum;
        public String createTime;
    }
}
