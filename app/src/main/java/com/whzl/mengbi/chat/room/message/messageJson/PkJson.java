package com.whzl.mengbi.chat.room.message.messageJson;

/**
 * @author nobody
 * @date 2018/9/27
 */
public class PkJson {

    /**
     * context : {"busiCode":"PK_ACCEPT_REQUEST","launchPkUserInfo":{"lastUpdateTime":1529054670000,"nickname":"土豪30000149","userId":30000149},"launchUserProgramId":100104,"pkSurPlusSecond":300,"pkUserInfo":{"lastUpdateTime":1537327157945,"nickname":"路奇","userId":30000139},"pkUserProgramId":100079}
     */

    public ContextBean context;

    public static class ContextBean {
        /**
         * busiCode : PK_ACCEPT_REQUEST
         * launchPkUserInfo : {"lastUpdateTime":1529054670000,"nickname":"土豪30000149","userId":30000149}
         * launchUserProgramId : 100104
         * pkSurPlusSecond : 300
         * pkUserInfo : {"lastUpdateTime":1537327157945,"nickname":"路奇","userId":30000139}
         * pkUserProgramId : 100079
         */

        public String busiCode;
        public LaunchPkUserInfoBean launchPkUserInfo;
        public int launchUserProgramId;
        public int pkSurPlusSecond;
        public LaunchPkUserInfoBean pkUserInfo;
        public int pkUserProgramId;
        public int pkUserScore;
        public int launchPkUserScore;
        public int pkTieSurplusSecond;
        public int pkPunishSurplusSecond;
        public long launchPkUserId;
        public long pkUserId;

        public static class LaunchPkUserInfoBean {
            /**
             * lastUpdateTime : 1529054670000
             * nickname : 土豪30000149
             * userId : 30000149
             */

            public long lastUpdateTime;
            public String nickname;
            public int userId;
        }
    }
}
