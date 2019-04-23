package com.whzl.mengbi.chat.room.message.messageJson;

import java.util.List;

/**
 * @author nobody
 * @date 2019/4/23
 */
public class RankChangeJson {


    /**
     * context : {"msgInfoList":[{"color":"FFFFFF","msgType":"IMG","msgValue":"https://static2.mengbitv.com/images/notice.png"},{"color":"FFFFFF","msgType":"TEXT","msgValue":"猜猜"},{"color":"FFFFFF","msgType":"TEXT","msgValue":"我是"},{"color":"FFFFFF","msgType":"TEXT","msgValue":"谁"}],"nickName":"我不是30000807","rank":1,"rankingsName":"百花争艳","ranksId":37158,"userId":"30000807"}
     * eventCode : RANK_CHANGE_MSG
     * msgType : EVENT
     * platform : WEB,MOBILE
     * type : busi.msg
     */

    public ContextBean context;
    public String eventCode;
    public String msgType;
    public String platform;
    public String type;

    public static class ContextBean {
        /**
         * msgInfoList : [{"color":"FFFFFF","msgType":"IMG","msgValue":"https://static2.mengbitv.com/images/notice.png"},{"color":"FFFFFF","msgType":"TEXT","msgValue":"猜猜"},{"color":"FFFFFF","msgType":"TEXT","msgValue":"我是"},{"color":"FFFFFF","msgType":"TEXT","msgValue":"谁"}]
         * nickName : 我不是30000807
         * rank : 1
         * rankingsName : 百花争艳
         * ranksId : 37158
         * userId : 30000807
         */

        public String nickName;
        public int rank;
        public String rankingsName;
        public int ranksId;
        public String userId;
        public List<MsgInfoListBean> msgInfoList;

        public static class MsgInfoListBean {
            /**
             * color : FFFFFF
             * msgType : IMG
             * msgValue : https://static2.mengbitv.com/images/notice.png
             */

            public String color;
            public String msgType;
            public String msgValue;
        }
    }
}
