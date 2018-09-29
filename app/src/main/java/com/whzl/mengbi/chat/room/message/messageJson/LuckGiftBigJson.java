package com.whzl.mengbi.chat.room.message.messageJson;

import java.util.List;

/**
 * @author nobody
 * @date 2018/9/27
 */
public class LuckGiftBigJson {

    /**
     * context : {"prizes":[{"giftPrice":100,"rewardRatio":10,"times":9},{"giftPrice":100,"rewardRatio":20,"times":11},{"giftPrice":100,"rewardRatio":500,"times":1}],"nickname":"土豪30000096","userId":30000096,"programId":100070}
     */

    public ContextBean context;

    public static class ContextBean {
        /**
         * prizes : [{"giftPrice":100,"rewardRatio":10,"times":9},{"giftPrice":100,"rewardRatio":20,"times":11},{"giftPrice":100,"rewardRatio":500,"times":1}]
         * nickname : 土豪30000096
         * userId : 30000096
         * programId : 100070
         */

        public String nickname;
        public int userId;
        public int programId;
        public List<PrizesBean> prizes;

        public static class PrizesBean {
            /**
             * giftPrice : 100
             * rewardRatio : 10
             * times : 9
             */

            public int giftPrice;
            public int rewardRatio;
            public int times;
        }
    }
}
