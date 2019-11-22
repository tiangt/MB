package com.whzl.mengbi.model.entity;

/**
 * @author nobody
 * @date 2019-11-22
 */
public class GiftBetPeriodList {

    /**
     * LuckBet : {"surplusSecond":116,"prizePoolNumber":0,"periodNumber":"20191122-262","goodsId":94320,"robStatus":"BETTING","uRobGame":{"id":1,"wealthType":"MENG_DOU","amount":1050,"limit":10,"createTime":"2019-03-21 13:15:52","multiple":10,"giftType":"S"},"periodId":87651,"goodsPic":"http://localtest-img.mengbitv.com/default/000/00/08/05_144x144.jpg","userBetCount":0,"periodTime":"150"}
     * OneInHundred : {"surplusSecond":null,"prizePoolNumber":2,"periodNumber":"20191120-1009","goodsId":94596,"robStatus":null,"uRobGame":{"id":3,"wealthType":"MENG_DOU","amount":52,"limit":10,"createTime":"2019-11-20 13:51:56","multiple":1,"giftType":"B"},"periodId":5,"goodsPic":"http://localtest-img.mengbitv.com/default/000/00/05/37_144x144.jpg","userBetCount":0,"periodTime":0}
     * userWealth : {"coin":1326021,"chengPonit":51226,"mengDou":67551051}
     */

    public LuckBetBean LuckBet;
    public OneInHundredBean OneInHundred;
    public UserWealthBean userWealth;

    public static class LuckBetBean {
        /**
         * surplusSecond : 116
         * prizePoolNumber : 0
         * periodNumber : 20191122-262
         * goodsId : 94320
         * robStatus : BETTING
         * uRobGame : {"id":1,"wealthType":"MENG_DOU","amount":1050,"limit":10,"createTime":"2019-03-21 13:15:52","multiple":10,"giftType":"S"}
         * periodId : 87651
         * goodsPic : http://localtest-img.mengbitv.com/default/000/00/08/05_144x144.jpg
         * userBetCount : 0
         * periodTime : 150
         */

        public long surplusSecond;
        public long prizePoolNumber;
        public String periodNumber;
        public long goodsId;
        public String robStatus;
        public URobGameBean uRobGame;
        public long periodId;
        public String goodsPic;
        public long userBetCount;
        public String periodTime;

        public static class URobGameBean {
            /**
             * id : 1
             * wealthType : MENG_DOU
             * amount : 1050
             * limit : 10
             * createTime : 2019-03-21 13:15:52
             * multiple : 10
             * giftType : S
             */

            public long id;
            public String wealthType;
            public long amount;
            public long limit;
            public String createTime;
            public long multiple;
            public String giftType;
        }
    }

    public static class OneInHundredBean {
        /**
         * surplusSecond : null
         * prizePoolNumber : 2
         * periodNumber : 20191120-1009
         * goodsId : 94596
         * robStatus : null
         * uRobGame : {"id":3,"wealthType":"MENG_DOU","amount":52,"limit":10,"createTime":"2019-11-20 13:51:56","multiple":1,"giftType":"B"}
         * periodId : 5
         * goodsPic : http://localtest-img.mengbitv.com/default/000/00/05/37_144x144.jpg
         * userBetCount : 0
         * periodTime : 0
         */

        public Object surplusSecond;
        public long prizePoolNumber;
        public String periodNumber;
        public long goodsId;
        public Object robStatus;
        public LuckBetBean.URobGameBean uRobGame;
        public long periodId;
        public String goodsPic;
        public long userBetCount;
        public long periodTime;
        public long requireGiftNum;
    }

    public static class UserWealthBean {
        /**
         * coin : 1326021
         * chengPonit : 51226
         * mengDou : 67551051
         */

        public long coin;
        public long chengPonit;
        public long mengDou;
    }
}
