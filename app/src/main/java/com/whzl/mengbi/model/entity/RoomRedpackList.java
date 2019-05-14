package com.whzl.mengbi.model.entity;

import java.util.List;

/**
 * @author nobody
 * @date 2019/1/16
 */
public class RoomRedpackList {

    public List<ListBean> list;

    public static class ListBean {
        /**
         * redPacketId : d/u0ke8lKYkRolE9bV4pGwRTDCWoHx3T0w9217BiEbxuLN631dMUV7P5uV42a8SA
         * objectId : 30000530
         * objectType : USER
         * redPacketType : RANDOM
         * amountTotal : 160000
         * deduct : 40000
         * redPacketQuantity : 5
         * contentType : COIN
         * goodsId : null
         * leftSeconds : 0
         * effDate : 1547537878703
         * expDate : 1547539678703
         * programId : 100147
         * founderUserNickname : null
         * gmtCreated : null
         * gmtModified : null
         */

        public String redPacketId;
        public int objectId;
        public String objectType;
        public String redPacketType;
        public int amountTotal;
        public int deduct;
        public int redPacketQuantity;
        public String contentType;
        public Object goodsId;
        public int leftSeconds;
        public long effDate;
        public long expDate;
        public int programId;
        public Object founderUserNickname;
        public Object gmtCreated;
        public Object gmtModified;
        public long currentTime = System.currentTimeMillis();
    }
}
