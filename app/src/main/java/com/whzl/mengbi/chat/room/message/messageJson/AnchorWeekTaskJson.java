package com.whzl.mengbi.chat.room.message.messageJson;

/**
 * @author nobody
 * @date 2018/12/13
 */
public class AnchorWeekTaskJson {

    /**
     * eventCode : ANCHOR_WEEK_TASK
     * msgType : EVENT
     * context : {"actionNeedValue":30,"actionValue":3,"actionAwardId":23093}
     */

    public String eventCode;
    public String msgType;
    public ContextBean context;

    public static class ContextBean {
        /**
         * actionNeedValue : 30
         * actionValue : 3
         * actionAwardId : 23093
         */

        public int actionNeedValue;
        public int actionValue;
        public int actionAwardId;
    }
}
