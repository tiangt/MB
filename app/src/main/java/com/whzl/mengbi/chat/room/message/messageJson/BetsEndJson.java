package com.whzl.mengbi.chat.room.message.messageJson;

/**
 * @author nobody
 * @date 2019/1/7
 */
public class BetsEndJson {


    /**
     * context : {"period":"2019-01-05-3","frozenCountDownSecond":300}
     */

    public ContextBean context;

    public static class ContextBean {
        /**
         * period : 2019-01-05-3
         * frozenCountDownSecond : 300
         */

        public String period;
        public long frozenCountDownSecond;
    }
}
