package com.whzl.mengbi.model.entity;

/**
 * @author nobody
 * @date 2019/1/22
 */
public class OpenRedBean {

    /**
     * code : 200
     * data : {"contentType":"COIN","userId":30000711,"goodsId":null,"amount":845,"goodsName":null}
     * message : 获取红包
     */

    public int code;
    public DataBean data;
    public String message;

    public static class DataBean {
        /**
         * contentType : COIN
         * userId : 30000711
         * goodsId : null
         * amount : 845
         * goodsName : null
         */

        public String contentType;
        public int userId;
        public Object goodsId;
        public long amount;
        public Object goodsName;
    }
}
