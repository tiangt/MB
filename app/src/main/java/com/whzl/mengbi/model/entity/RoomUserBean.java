package com.whzl.mengbi.model.entity;

import java.util.List;

/**
 * @author nobody
 * @date 2018/12/10
 */
public class RoomUserBean {

    /**
     * userId : 30000711
     * userType : VIEWER
     * nickname : 凤凰传奇
     * avatar : https://test-img.mengbitv.com/avatar/030/00/07/11_100x100.jpg?1544173086
     * sex : M
     * introduce :
     * birthday : 1900-01-01 00:00:00
     * province : 浙江省
     * city : 杭州市
     * levelList : [{"levelType":"ROYAL_LEVEL","levelValue":4,"levelName":"铂金","expList":[{"expType":"ROYAL_EXP","expName":"贵族经验","sjExpvalue":4000,"bjExpValue":0,"sjNeedExpValue":10000,"bjNeedExpValue":2000}]},{"levelType":"USER_LEVEL","levelValue":26,"levelName":"公爵","expList":[{"expType":"USER_EXP","expName":"用户经验","sjExpvalue":46903080,"bjExpValue":126893268,"sjNeedExpValue":50000000,"bjNeedExpValue":0}]}]
     * isSubs : true
     * identityId : 70
     * disabledService : []
     * propertyMap : {}
     * goodsList : [{"goodsId":301,"goodsName":"vip","goodsType":"VIP","goodsIcon":"https://test-img.mengbitv.com/default/000/00/03/29.jpg","bindProgramId":0},{"goodsId":94372,"goodsName":"富豪榜周榜第4名","goodsType":"BADGE","goodsIcon":"https://test-img.mengbitv.com/default/000/00/02/59.jpg","bindProgramId":0},{"goodsId":94450,"goodsName":"星光达人","goodsType":"BADGE","goodsIcon":"https://test-img.mengbitv.com/default/000/00/03/85.jpg","bindProgramId":0},{"goodsId":94537,"goodsName":"妖姬卡","goodsType":"DEMON_CARD","goodsIcon":"https://test-img.mengbitv.com/default/000/00/00/00.jpg","bindProgramId":0},{"goodsId":94352,"goodsName":"保时捷911","goodsType":"CAR","goodsIcon":"https://test-img.mengbitv.com/default/000/00/02/69.jpg","bindProgramId":0}]
     * weathMap : {"coin":109999870287530,"point":0}
     * rank : 15
     * isFollowed : F
     */

    public int userId;
    public String userType;
    public String nickname;
    public String avatar;
    public String sex;
    public String introduce;
    public String birthday;
    public String province;
    public String city;
    public boolean isSubs;
    public int identityId;
    public PropertyMapBean propertyMap;
    public WeathMapBean weathMap;
    public int rank;
    public String isFollowed;
    public List<LevelListBean> levelList;
    public List<?> disabledService;
    public List<GoodsListBean> goodsList;

    public static class PropertyMapBean {
    }

    public static class WeathMapBean {
        /**
         * coin : 109999870287530
         * point : 0
         */

        public long coin;
        public int point;
    }

    public static class LevelListBean {
        /**
         * levelType : ROYAL_LEVEL
         * levelValue : 4
         * levelName : 铂金
         * expList : [{"expType":"ROYAL_EXP","expName":"贵族经验","sjExpvalue":4000,"bjExpValue":0,"sjNeedExpValue":10000,"bjNeedExpValue":2000}]
         */

        public String levelType;
        public int levelValue;
        public String levelName;
        public List<ExpListBean> expList;

        public static class ExpListBean {
            /**
             * expType : ROYAL_EXP
             * expName : 贵族经验
             * sjExpvalue : 4000
             * bjExpValue : 0
             * sjNeedExpValue : 10000
             * bjNeedExpValue : 2000
             */

            public String expType;
            public String expName;
            public int sjExpvalue;
            public int bjExpValue;
            public int sjNeedExpValue;
            public int bjNeedExpValue;
        }
    }

    public static class GoodsListBean {
        /**
         * goodsId : 301
         * goodsName : vip
         * goodsType : VIP
         * goodsIcon : https://test-img.mengbitv.com/default/000/00/03/29.jpg
         * bindProgramId : 0
         */

        public int goodsId;
        public String goodsName;
        public String goodsType;
        public String goodsIcon;
        public int bindProgramId;
    }
}
