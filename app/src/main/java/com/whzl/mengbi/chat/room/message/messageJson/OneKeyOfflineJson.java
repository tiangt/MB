package com.whzl.mengbi.chat.room.message.messageJson;

import java.util.List;

/**
 * @author nobody
 * @date 2019/5/17
 */
public class OneKeyOfflineJson {

    /**
     * context : {"busicode":"ONE_KEY_OFFLINE","sessionList":[{"sessionId":"30946e28351da5e570b5a6a318b12950","userId":30000711}],"userId":30000711}
     * eventCode : ONE_KEY_OFFLINE
     * msgType : EVENT
     * platform : WEB,MOBILE
     * type : busi.msg
     */

    public ContextBean context;
    public String eventCode;
    public String msgType;
    public String platform;
    public String type;

    public static class ContextBean {
        /**
         * busicode : ONE_KEY_OFFLINE
         * sessionList : [{"sessionId":"30946e28351da5e570b5a6a318b12950","userId":30000711}]
         * userId : 30000711
         */

        public String busicode;
        public int userId;
        public List<SessionListBean> sessionList;

        public static class SessionListBean {
            /**
             * sessionId : 30946e28351da5e570b5a6a318b12950
             * userId : 30000711
             */

            public String sessionId;
            public int userId;
        }
    }
}
