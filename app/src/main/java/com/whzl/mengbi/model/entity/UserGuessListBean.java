package com.whzl.mengbi.model.entity;

import java.util.List;

/**
 * @author nobody
 * @date 2019-06-14
 */
public class UserGuessListBean {

    public List<ListBean> list;

    public static class ListBean {
        /**
         * uGameGuess : {"guessId":35,"userId":30000139,"guessTheme":"A与B","squareArgument":"A","counterArgument":"B","squareArgumentFee":1000,"counterArgumentFee":0,"squareOdds":1,"counterOdds":100,"successArgument":"SQUARE_ARGUMENT","status":"FINISH","closingTime":"2019-06-12 17:11:52","nextTime":"2019-06-12 17:11:52","busiStatus":"FINISH","isRepeated":"F","gmtCreated":"2019-06-12 17:02:37","isModified":"F"}
         * uGameGuessObject : {"guessId":35,"userId":30000139,"guessBettingScheme":"SQUARE_ARGUMENT","fee":1000,"produce":0,"gmtCreated":"2019-06-12 17:03:53"}
         */

        public UGameGuessBean uGameGuess;
        public UGameGuessObjectBean uGameGuessObject;

        public static class UGameGuessBean {
            /**
             * guessId : 35
             * userId : 30000139
             * guessTheme : A与B
             * squareArgument : A
             * counterArgument : B
             * squareArgumentFee : 1000
             * counterArgumentFee : 0
             * squareOdds : 1
             * counterOdds : 100
             * successArgument : SQUARE_ARGUMENT
             * status : FINISH
             * closingTime : 2019-06-12 17:11:52
             * nextTime : 2019-06-12 17:11:52
             * busiStatus : FINISH
             * isRepeated : F
             * gmtCreated : 2019-06-12 17:02:37
             * isModified : F
             */

            public long guessId;
            public int userId;
            public String guessTheme;
            public String squareArgument;
            public String counterArgument;
            public double squareArgumentFee;
            public double counterArgumentFee;
            public double squareOdds;
            public double counterOdds;
            public String successArgument;
            public String status;
            public String closingTime;
            public String nextTime;
            public String busiStatus;
            public String isRepeated;
            public String gmtCreated;
            public String isModified;
        }

        public static class UGameGuessObjectBean {
            /**
             * guessId : 35
             * userId : 30000139
             * guessBettingScheme : SQUARE_ARGUMENT
             * fee : 1000
             * produce : 0
             * gmtCreated : 2019-06-12 17:03:53
             */

            public int guessId;
            public int userId;
            public String guessBettingScheme;
            public int fee;
            public int produce;
            public String gmtCreated;
        }
    }
}
