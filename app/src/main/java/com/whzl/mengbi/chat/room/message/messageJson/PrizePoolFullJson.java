package com.whzl.mengbi.chat.room.message.messageJson;

/**
 * @author nobody
 * @date 2019/1/7
 */
public class PrizePoolFullJson {

    /**
     * context : {"countDownSecond":1800,"periodNumber":"2019-01-07-1","prizePoolNumber":380000}
     */

    public ContextBean context;

    public static class ContextBean {
        /**
         * countDownSecond : 1800
         * periodNumber : 2019-01-07-1
         * prizePoolNumber : 380000
         */

        public long countDownSecond;
        public String periodNumber;
        public int prizePoolNumber;
    }
}
