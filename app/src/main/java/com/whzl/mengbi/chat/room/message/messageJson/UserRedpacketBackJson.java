package com.whzl.mengbi.chat.room.message.messageJson;

/**
 * @author nobody
 * @date 2019-09-25
 */
public class UserRedpacketBackJson {

    /**
     * context : {"nickname":"꧁电饭煲꧂","busicode":"USER_REDPACKET_BACK","userId":30000806,"lastUpdateTime":1563867657239}
     */

    public ContextBean context;

    public static class ContextBean {
        /**
         * nickname : ꧁电饭煲꧂
         * busicode : USER_REDPACKET_BACK
         * userId : 30000806
         * lastUpdateTime : 1563867657239
         */

        public String nickname;
        public String busicode;
        public int userId;
        public long lastUpdateTime;
    }
}
