package com.whzl.mengbi.chat.room.message.messageJson;

/**
 * @author nobody
 * @date 2019-06-13
 */
public class GuessJson {

    /**
     * context : {"busicode":"USER_GUESS","UGameGuessDto":{"counterArgumentFee":0,"squareArgumentFee":100,"gmtCreated":1560235515000,"nextTime":1560253601000,"squareArgument":"等待","counterOdds":100,"isRepeated":"F","userId":30000012,"counterArgument":"方法","closingTime":1560253515000,"squareOdds":1,"guessTheme":"受到第三方","busiStatus":"BET","guessId":24,"status":"BET"},"programId":100001}
     * type : busi.msg
     * platform : WEB,MOBILE
     */

    public ContextBean context;
    public String type;
    public String platform;

    public static class ContextBean {
        /**
         * busicode : USER_GUESS
         * UGameGuessDto : {"counterArgumentFee":0,"squareArgumentFee":100,"gmtCreated":1560235515000,"nextTime":1560253601000,"squareArgument":"等待","counterOdds":100,"isRepeated":"F","userId":30000012,"counterArgument":"方法","closingTime":1560253515000,"squareOdds":1,"guessTheme":"受到第三方","busiStatus":"BET","guessId":24,"status":"BET"}
         * programId : 100001
         */

        public String busicode;
        public UGameGuessDtoBean UGameGuessDto;
        public int programId;

        public static class UGameGuessDtoBean {
            /**
             * counterArgumentFee : 0
             * squareArgumentFee : 100
             * gmtCreated : 1560235515000
             * nextTime : 1560253601000
             * squareArgument : 等待
             * counterOdds : 100
             * isRepeated : F
             * userId : 30000012
             * counterArgument : 方法
             * closingTime : 1560253515000
             * squareOdds : 1
             * guessTheme : 受到第三方
             * busiStatus : BET
             * guessId : 24
             * status : BET
             */

            public double counterArgumentFee;
            public double squareArgumentFee;
            public long gmtCreated;
            public long nextTime;
            public String squareArgument;
            public double counterOdds;
            public String isRepeated;
            public int userId;
            public String counterArgument;
            public long closingTime;
            public double squareOdds;
            public String guessTheme;
            public String busiStatus;
            public String successArgument;
            public int guessId;
            public String status;
            public String guessType;
        }
    }
}
