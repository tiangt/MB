package com.whzl.mengbi.chat.room.message.messageJson;

/**
 * @author nobody
 * @date 2019/5/30
 */
public class FlopCardJson {

    /**
     * context : {"expNumber":1000,"nickName":"mengbi","price":50,"userLuckyNumber":-538150,"prizeType":"EXP","expType":"USER_EXP"}
     * type : busi.msg
     * platform : WEB,MOBILE
     */

    public ContextBean context;
    public String type;
    public String platform;

    public static class ContextBean {
        /**
         * expNumber : 1000
         * nickName : mengbi
         * price : 50
         * userLuckyNumber : -538150
         * prizeType : EXP
         * expType : USER_EXP
         */

        public int expNumber;
        public String nickName;
        public int price;
        public int wealthNumber;
        public int goodsCount;
        public int userLuckyNumber;
        public String prizeType;
        public String expType;
        public String goodsName;
        public String wealthType;
    }
}
