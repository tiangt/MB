package com.whzl.mengbi.chat.room.message.messageJson;

/**
 * @author nobody
 * @date 2019-11-22
 */
public class RobBigLuckyJson {

    /**
     * context : {"prizePoolNumber":10,"busiCode":"big_prize_pool_change"}
     */

    public ContextBean context;

    public static class ContextBean {
        /**
         * prizePoolNumber : 10
         * busiCode : big_prize_pool_change
         */

        public int prizePoolNumber;
        public String busiCode;
    }
}
