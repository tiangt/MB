package com.whzl.mengbi.model.entity;

import java.util.List;

/**
 * @author nobody
 * @date 2019-06-12
 */
public class GameGuessBean {

    public List<ListBean> list;

    public static class ListBean {
        /**
         * guessId : 25
         * userId : 30000139
         * guessTheme : A与B
         * squareArgument : A
         * counterArgument : B
         * squareArgumentFee : 0
         * counterArgumentFee : 0
         * squareOdds : 1
         * counterOdds : 1
         * successArgument :
         * status : FINSH
         * closingTime : 2019-06-11 16:00:00
         * gmtCreated : 2019-06-11 14:59:02
         * gmtModified : null
         * isModified : F
         */

        public int guessId;
        public int userId;
        public String guessTheme;
        public String squareArgument;
        public String counterArgument;
        public int squareArgumentFee;
        public int counterArgumentFee;
        public int squareOdds;
        public int counterOdds;
        public String successArgument;
        public String status;
        public String closingTime;
        public String gmtCreated;
        public Object gmtModified;
        public String isModified;
    }
}
