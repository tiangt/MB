package com.whzl.mengbi.model.entity;

/**
 * @author nobody
 * @date 2019-11-21
 */
public class PkGuessBean {

    /**
     * guessObj : {"guessId":423,"userId":30000804,"guessTheme":"本场PK哪方获胜","squareArgument":"昵称很长哼唱昵称哼唱","counterArgument":"美丽","squareArgumentFee":0,"counterArgumentFee":0,"squareOdds":1,"counterOdds":1,"successArgument":"","status":"BET","guessType":"PK_GUESS","closingTime":"2019-11-19 17:26:26","gmtCreated":"2019-11-19 17:18:26","gmtModified":null,"isModified":"F"}
     */

    public GuessObjBean guessObj;

    public static class GuessObjBean {
        /**
         * guessId : 423
         * userId : 30000804
         * guessTheme : 本场PK哪方获胜
         * squareArgument : 昵称很长哼唱昵称哼唱
         * counterArgument : 美丽
         * squareArgumentFee : 0
         * counterArgumentFee : 0
         * squareOdds : 1
         * counterOdds : 1
         * successArgument :
         * status : BET
         * guessType : PK_GUESS
         * closingTime : 2019-11-19 17:26:26
         * gmtCreated : 2019-11-19 17:18:26
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
        public double squareOdds;
        public double counterOdds;
        public String successArgument;
        public String status;
        public String guessType;
        public String closingTime;
        public String gmtCreated;
        public Object gmtModified;
        public String isModified;
    }
}
