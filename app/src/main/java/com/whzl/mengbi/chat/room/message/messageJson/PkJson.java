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
        /**
         * firstBloodUserDto : {"nickname":"zhansan","userId":"137068","lastUpdateTime":"2018-10-30 11:11:11"}
         * programId : 100079
         */

        public FirstBloodUserDtoBean firstBloodUserDto;
        public int programId;

        public String mvpNickname;
        public String result;
        public String launchPkUserNickname;
        public String pkUserNickname;

        public static class LaunchPkUserInfoBean {
            /**
             * lastUpdateTime : 1529054670000
             * nickname : 土豪30000149
             * userId : 30000149
             * avatar: "http://test-img.mengbitv.com/avatar/030/00/00/12_100x100.jpg?1529897703"
             */

            public long lastUpdateTime;
            public String nickname;
            public int userId;
            public String avatar;
        }


        public static class FirstBloodUserDtoBean {
            /**
             * nickname : zhansan
             * userId : 137068
             * lastUpdateTime : 2018-10-30 11:11:11
             */

            public String nickname;
            public String userId;
            public String lastUpdateTime;
        }
    }
}
