package com.whzl.mengbi.chat.room.message.messageJson;

import com.whzl.mengbi.model.entity.PKFansBean;

import java.util.List;

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

        public int changeUserProgramId;
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
        public String punishWay;
        /**
         * pkUserLiveAndStreamAddress : {"liveTypeId":1,"showStreams":[{"streamType":"rtmp","streamAddress":"rtmp://livedown.mengbitv.com/live/100144"},{"streamType":"flv","streamAddress":"http://livedown.mengbitv.com/live/100144.flv"},{"streamType":"hls","streamAddress":"http://livedown.mengbitv.com/live/100144/playlist.m3u8"}],"liveType":"PC","platformType":"ENT","width":800,"height":600}
         */

        public PkUserLiveAndStreamAddressBean pkUserLiveAndStreamAddress;
        public PkUserLiveAndStreamAddressBean launchPkUserLiveAndStreamAddress;


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

        public static class PkUserLiveAndStreamAddressBean {
            /**
             * liveTypeId : 1
             * showStreams : [{"streamType":"rtmp","streamAddress":"rtmp://livedown.mengbitv.com/live/100144"},{"streamType":"flv","streamAddress":"http://livedown.mengbitv.com/live/100144.flv"},{"streamType":"hls","streamAddress":"http://livedown.mengbitv.com/live/100144/playlist.m3u8"}]
             * liveType : PC
             * platformType : ENT
             * width : 800
             * height : 600
             */

            public int liveTypeId;
            public String liveType;
            public String platformType;
            public int width;
            public int height;
            public List<ShowStreamsBean> showStreams;

            public static class ShowStreamsBean {
                /**
                 * streamType : rtmp
                 * streamAddress : rtmp://livedown.mengbitv.com/live/100144
                 */

                public String streamType;
                public String streamAddress;
            }
        }

        public MvpUserBean mvpUser;

        public static class MvpUserBean {
            /**
             * userId : 30000607
             * nickname : 萌友30000607
             * avatar : http://test-img.mengbitv.com/avatar/030/00/06/07_100x100.jpg?1532419849
             */

            public int userId;
            public String nickname;
            public String avatar;
        }


        public List<PKFansBean> userFans;

    }
}
