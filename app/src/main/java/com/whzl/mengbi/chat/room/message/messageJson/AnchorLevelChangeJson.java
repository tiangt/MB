package com.whzl.mengbi.chat.room.message.messageJson;

import java.util.List;

/**
 * @author nobody
 * @date 2018/9/27
 */
public class AnchorLevelChangeJson {
    /**
     * context : {"deliverDTO":{"count":1,"deliverId":30000139,"goodsName":"亲一个","lastUpdateTime":1537327157945,"nickname":"路奇","picId":128},"lastUpdateTime":1529897114000,"levelType":"ANCHOR_LEVEL","levels":[{"beginTime":1538201058000,"expList":[{"bjExpValue":254562,"expName":"付费礼物经验","expType":"GIFT_EXP","sjExpvalue":254562,"sjNeedExpValue":350000,"totalExpValue":254562,"userExpSn":575}],"levelName":"黄钻1","levelPic":"0","levelType":"ANCHOR_LEVEL","levelValue":11,"remark":"","userLevelSn":505},{"beginTime":1533052803000,"expList":[{"bjExpValue":0,"bjNeedExpValue":0,"expName":"贵族经验","expType":"ROYAL_EXP","sjExpvalue":0,"sjNeedExpValue":200,"totalExpValue":0,"userExpSn":572}],"levelName":"平民","levelPic":"0","levelType":"ROYAL_LEVEL","levelValue":0,"remark":"","userLevelSn":502},{"beginTime":1530754885000,"expList":[{"bjExpValue":1425900,"expName":"用户经验","expType":"USER_EXP","sjExpvalue":1425900,"sjNeedExpValue":1600000,"totalExpValue":1425900,"userExpSn":571}],"levelName":"富甲16","levelPic":"0","levelType":"USER_LEVEL","levelValue":17,"remark":"","userLevelSn":501}],"nickname":"土豪30000216","programId":100073,"userId":30000216,"userType":"ANCHOR"}
     * display :
     * eventCode : ANCHOR_LEVEL_CHANGE_BROADCAST
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
         * deliverDTO : {"count":1,"deliverId":30000139,"goodsName":"亲一个","lastUpdateTime":1537327157945,"nickname":"路奇","picId":128}
         * lastUpdateTime : 1529897114000
         * levelType : ANCHOR_LEVEL
         * levels : [{"beginTime":1538201058000,"expList":[{"bjExpValue":254562,"expName":"付费礼物经验","expType":"GIFT_EXP","sjExpvalue":254562,"sjNeedExpValue":350000,"totalExpValue":254562,"userExpSn":575}],"levelName":"黄钻1","levelPic":"0","levelType":"ANCHOR_LEVEL","levelValue":11,"remark":"","userLevelSn":505},{"beginTime":1533052803000,"expList":[{"bjExpValue":0,"bjNeedExpValue":0,"expName":"贵族经验","expType":"ROYAL_EXP","sjExpvalue":0,"sjNeedExpValue":200,"totalExpValue":0,"userExpSn":572}],"levelName":"平民","levelPic":"0","levelType":"ROYAL_LEVEL","levelValue":0,"remark":"","userLevelSn":502},{"beginTime":1530754885000,"expList":[{"bjExpValue":1425900,"expName":"用户经验","expType":"USER_EXP","sjExpvalue":1425900,"sjNeedExpValue":1600000,"totalExpValue":1425900,"userExpSn":571}],"levelName":"富甲16","levelPic":"0","levelType":"USER_LEVEL","levelValue":17,"remark":"","userLevelSn":501}]
         * nickname : 土豪30000216
         * programId : 100073
         * userId : 30000216
         * userType : ANCHOR
         */

        public DeliverDTOBean deliverDTO;
        public long lastUpdateTime;
        public String levelType;
        public String nickname;
        public int programId;
        public int userId;
        public String userType;
        public List<LevelsBean> levels;

        public static class DeliverDTOBean {
            /**
             * count : 1
             * deliverId : 30000139
             * goodsName : 亲一个
             * lastUpdateTime : 1537327157945
             * nickname : 路奇
             * picId : 128
             */

            public int count;
            public int deliverId;
            public String goodsName;
            public long lastUpdateTime;
            public String nickname;
            public int picId;
        }

        public static class LevelsBean {
            /**
             * beginTime : 1538201058000
             * expList : [{"bjExpValue":254562,"expName":"付费礼物经验","expType":"GIFT_EXP","sjExpvalue":254562,"sjNeedExpValue":350000,"totalExpValue":254562,"userExpSn":575}]
             * levelName : 黄钻1
             * levelPic : 0
             * levelType : ANCHOR_LEVEL
             * levelValue : 11
             * remark :
             * userLevelSn : 505
             */

            public long beginTime;
            public String levelName;
            public String levelPic;
            public String levelType;
            public int levelValue;
            public String remark;
            public int userLevelSn;
            public List<ExpListBean> expList;

            public static class ExpListBean {
                /**
                 * bjExpValue : 254562
                 * expName : 付费礼物经验
                 * expType : GIFT_EXP
                 * sjExpvalue : 254562
                 * sjNeedExpValue : 350000
                 * totalExpValue : 254562
                 * userExpSn : 575
                 */

                public long bjExpValue;
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
