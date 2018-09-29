package com.whzl.mengbi.chat.room.message.messageJson;

import java.util.List;

/**
 * @author nobody
 * @date 2018/9/27
 */
public class RoyalLevelChangeJson {


    /**
     * context : {"levelType":"ROYAL_LEVEL","levels":[{"beginTime":1538191676000,"bjBeginTime":1538323200000,"expList":[{"bjExpValue":200,"bjNeedExpValue":100,"expName":"贵族经验","expType":"ROYAL_EXP","sjExpvalue":200,"sjNeedExpValue":500,"totalExpValue":200,"userExpSn":1857}],"levelName":"青铜","levelPic":"0","levelType":"ROYAL_LEVEL","levelValue":1,"remark":"","userLevelSn":1635},{"beginTime":1538105897000,"expList":[{"bjExpValue":0,"expName":"用户经验","expType":"USER_EXP","sjExpvalue":0,"sjNeedExpValue":1000,"totalExpValue":0,"userExpSn":1856}],"levelName":"平民0","levelPic":"0","levelType":"USER_LEVEL","levelValue":1,"remark":"","userLevelSn":1634}],"nickname":"萌友30000716","userId":30000716,"userType":"VIEWER"}
     * display :
     * eventCode : ROYAL_LEVEL_CHANGE_BROADCAST
     * msgType : EVENT
     * platform : WEB,MOBILE
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
         * levelType : ROYAL_LEVEL
         * levels : [{"beginTime":1538191676000,"bjBeginTime":1538323200000,"expList":[{"bjExpValue":200,"bjNeedExpValue":100,"expName":"贵族经验","expType":"ROYAL_EXP","sjExpvalue":200,"sjNeedExpValue":500,"totalExpValue":200,"userExpSn":1857}],"levelName":"青铜","levelPic":"0","levelType":"ROYAL_LEVEL","levelValue":1,"remark":"","userLevelSn":1635},{"beginTime":1538105897000,"expList":[{"bjExpValue":0,"expName":"用户经验","expType":"USER_EXP","sjExpvalue":0,"sjNeedExpValue":1000,"totalExpValue":0,"userExpSn":1856}],"levelName":"平民0","levelPic":"0","levelType":"USER_LEVEL","levelValue":1,"remark":"","userLevelSn":1634}]
         * nickname : 萌友30000716
         * userId : 30000716
         * userType : VIEWER
         */

        public String levelType;
        public String nickname;
        public int userId;
        public String userType;
        public List<LevelsBean> levels;

        public static class LevelsBean {
            /**
             * beginTime : 1538191676000
             * bjBeginTime : 1538323200000
             * expList : [{"bjExpValue":200,"bjNeedExpValue":100,"expName":"贵族经验","expType":"ROYAL_EXP","sjExpvalue":200,"sjNeedExpValue":500,"totalExpValue":200,"userExpSn":1857}]
             * levelName : 青铜
             * levelPic : 0
             * levelType : ROYAL_LEVEL
             * levelValue : 1
             * remark :
             * userLevelSn : 1635
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
                 * bjExpValue : 200
                 * bjNeedExpValue : 100
                 * expName : 贵族经验
                 * expType : ROYAL_EXP
                 * sjExpvalue : 200
                 * sjNeedExpValue : 500
                 * totalExpValue : 200
                 * userExpSn : 1857
                 */

                public int bjExpValue;
                public int bjNeedExpValue;
                public String expName;
                public String expType;
                public int sjExpvalue;
                public int sjNeedExpValue;
                public int totalExpValue;
                public int userExpSn;
            }
        }
    }
}
