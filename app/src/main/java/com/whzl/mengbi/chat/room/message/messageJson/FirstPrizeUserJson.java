package com.whzl.mengbi.chat.room.message.messageJson;

import java.util.List;

/**
 * @author nobody
 * @date 2019/1/7
 */
public class FirstPrizeUserJson {


    /**
     * context : {"firstPrizeTotalValue":200,"userNickNameList":["说好的心腹","明天你好"]}
     */

    public ContextBean context;

    public static class ContextBean {
        /**
         * firstPrizeTotalValue : 200
         * userNickNameList : ["说好的心腹","明天你好"]
         */

        public long firstPrizeTotalValue;
        public List<String> userNickNameList;
    }
}
