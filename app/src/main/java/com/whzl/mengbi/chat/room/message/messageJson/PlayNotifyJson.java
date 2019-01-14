package com.whzl.mengbi.chat.room.message.messageJson;

/**
 * @author nobody
 * @date 2019/1/7
 */
public class PlayNotifyJson {


    /**
     * context : {"lastUpdateTime":1545962444559,"nickname":"美丽","programId":100120,"programName":"30000150","programPicId":"355","userId":30000150}
     */

    public ContextBean context;

    public static class ContextBean {
        /**
         * lastUpdateTime : 1545962444559
         * nickname : 美丽
         * programId : 100120
         * programName : 30000150
         * programPicId : 355
         * userId : 30000150
         */

        public long lastUpdateTime;
        public String nickname;
        public int programId;
        public String programName;
        public String programPicId;
        public long userId;
    }
}
