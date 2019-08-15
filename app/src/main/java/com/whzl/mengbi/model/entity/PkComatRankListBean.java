package com.whzl.mengbi.model.entity;

import java.util.List;

/**
 * @author nobody
 * @date 2019-08-15
 */
public class PkComatRankListBean {

    public List<ListBean> list;

    public static class ListBean {
        /**
         * programId : 100174
         * programName : 萌友30000809
         * status : T
         * roomUserCount : 0
         * cover : http://localtest-img.mengbitv.com/default/000/00/00/00_400x400.jpg
         * anchorId : 30000809
         * anchorNickname : Youiil
         * anchorLevelName : 皇冠16
         * anchorLevelValue : 31
         * anchorAvatar : http://localtest-img.mengbitv.com/avatar/030/00/08/09_100x100.jpg?1565688521
         * showStreamData : {"height":540,"width":960,"liveType":"PC","rtmp":"rtmp://livedown.mengbitv.com/live/100174","flv":"http://livedown.mengbitv.com/live/100174.flv","hls":"http://livedown.mengbitv.com/live/100174/playlist.m3u8"}
         * lastShowBeginTime : 2019-04-17 15:53:40
         * isPk : F
         * isWeekStar : T
         * isPopularity : F
         * rankInfo : {"combatValue":542,"rankName":"高手V","anchorVictoryRatio":0,"bestTeamUser":[{"userId":30000809,"lastUpdateTime":"2019-06-20 10:24:07","nickname":"Youiil","contributeScore":5127876},{"userId":30000154,"lastUpdateTime":"2019-04-25 10:11:43","nickname":"揉一揉","contributeScore":131400}]}
         */

        public int programId;
        public String programName;
        public String status;
        public int roomUserCount;
        public String cover;
        public int anchorId;
        public String anchorNickname;
        public String anchorLevelName;
        public long anchorLevelValue;
        public String anchorAvatar;
        public ShowStreamDataBean showStreamData;
        public String lastShowBeginTime;
        public String isPk;
        public String isWeekStar;
        public String isPopularity;
        public RankInfoBean rankInfo;

        public static class ShowStreamDataBean {
            /**
             * height : 540
             * width : 960
             * liveType : PC
             * rtmp : rtmp://livedown.mengbitv.com/live/100174
             * flv : http://livedown.mengbitv.com/live/100174.flv
             * hls : http://livedown.mengbitv.com/live/100174/playlist.m3u8
             */

            public int height;
            public int width;
            public String liveType;
            public String rtmp;
            public String flv;
            public String hls;
        }

        public static class RankInfoBean {
            /**
             * combatValue : 542
             * rankName : 高手V
             * anchorVictoryRatio : 0
             * bestTeamUser : [{"userId":30000809,"lastUpdateTime":"2019-06-20 10:24:07","nickname":"Youiil","contributeScore":5127876},{"userId":30000154,"lastUpdateTime":"2019-04-25 10:11:43","nickname":"揉一揉","contributeScore":131400}]
             */

            public long combatValue;
            public int rankId;
            public String rankName;
            public int anchorVictoryRatio;
            public List<BestTeamUserBean> bestTeamUser;

            public static class BestTeamUserBean {
                /**
                 * userId : 30000809
                 * lastUpdateTime : 2019-06-20 10:24:07
                 * nickname : Youiil
                 * contributeScore : 5127876
                 */

                public int userId;
                public String lastUpdateTime;
                public String nickname;
                public long contributeScore;
            }
        }
    }
}
