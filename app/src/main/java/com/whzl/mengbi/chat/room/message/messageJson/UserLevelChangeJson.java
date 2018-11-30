package com.whzl.mengbi.chat.room.message.messageJson;

import java.util.List;

/**
 * @author nobody
 * @date 2018/9/27
 */
public class UserLevelChangeJson {


    /**
     * context : {"lastUpdateTime":1538120824152,"levelType":"USER_LEVEL","levels":[{"beginTime":1538120901000,"bjBeginTime":1538323200000,"expList":[{"bjExpValue":10000,"bjNeedExpValue":4000,"expName":"贵族经验","expType":"ROYAL_EXP","sjExpvalue":10000,"sjNeedExpValue":20000,"totalExpValue":10000,"userExpSn":1861}],"levelName":"钻石","levelPic":"0","levelType":"ROYAL_LEVEL","levelValue":5,"remark":"","userLevelSn":1639},{"beginTime":1538189017000,"expList":[{"bjExpValue":10709488,"expName":"用户经验","expType":"USER_EXP","sjExpvalue":200000,"sjNeedExpValue":300000,"totalExpValue":200000,"userExpSn":1860}],"levelName":"富商11","levelPic":"0","levelType":"USER_LEVEL","levelValue":12,"remark":"","userLevelSn":1638}],"nickname":"萌友30000718","oldLevel":{"levelName":"富豪10","levelValue":11},"userId":30000718,"userType":"VIEWER"}
     * display :
     * eventCode : USER_LEVEL_CHANGE_BROADCAST
     * msgType : EVENT
     * platform : MOBILE
     * type : busi.msg
     */

    public ContextBean context;
    public String display;
    public String eventCode;
    public String msgType;
    public String platform;
    public String type;

    public static class ContextBean {
        /**
         * lastUpdateTime : 1538120824152
         * levelType : USER_LEVEL
         * levels : [{"beginTime":1538120901000,"bjBeginTime":1538323200000,"expList":[{"bjExpValue":10000,"bjNeedExpValue":4000,"expName":"贵族经验","expType":"ROYAL_EXP","sjExpvalue":10000,"sjNeedExpValue":20000,"totalExpValue":10000,"userExpSn":1861}],"levelName":"钻石","levelPic":"0","levelType":"ROYAL_LEVEL","levelValue":5,"remark":"","userLevelSn":1639},{"beginTime":1538189017000,"expList":[{"bjExpValue":10709488,"expName":"用户经验","expType":"USER_EXP","sjExpvalue":200000,"sjNeedExpValue":300000,"totalExpValue":200000,"userExpSn":1860}],"levelName":"富商11","levelPic":"0","levelType":"USER_LEVEL","levelValue":12,"remark":"","userLevelSn":1638}]
         * nickname : 萌友30000718
         * oldLevel : {"levelName":"富豪10","levelValue":11}
         * userId : 30000718
         * userType : VIEWER
         */

        public long lastUpdateTime;
        public String levelType;
        public String nickname;
        public OldLevelBean oldLevel;
        public int userId;
        public String userType;
        public List<LevelsBean> levels;

        public static class OldLevelBean {
            /**
             * levelName : 富豪10
             * levelValue : 11
             */

            public String levelName;
            public int levelValue;
        }

        public static class LevelsBean {
            /**
             * beginTime : 1538120901000
             * bjBeginTime : 1538323200000
             * expList : [{"bjExpValue":10000,"bjNeedExpValue":4000,"expName":"贵族经验","expType":"ROYAL_EXP","sjExpvalue":10000,"sjNeedExpValue":20000,"totalExpValue":10000,"userExpSn":1861}]
             * levelName : 钻石
             * levelPic : 0
             * levelType : ROYAL_LEVEL
             * levelValue : 5
             * remark :
             * userLevelSn : 1639
             */

            public long beginTime;
            public long bjBeginTime;
            public String levelName;
            public String levelPic;
            public String levelType;
            public int levelValue;
            public String remark;
            public int userLevelSn;
            public List<ExpListBean> expList;

            public static class ExpListBean {
                /**
                 * bjExpValue : 10000
                 * bjNeedExpValue : 4000
                 * expName : 贵族经验
                 * expType : ROYAL_EXP
                 * sjExpvalue : 10000
                 * sjNeedExpValue : 20000
                 * totalExpValue : 10000
                 * userExpSn : 1861
                 */

                public long bjExpValue;
                public long bjNeedExpValue;
                public String expName;
                public String expType;
                public long sjExpvalue;
                public long sjNeedExpValue;
                public long totalExpValue;
                public int userExpSn;
            }
        }
    }
}
