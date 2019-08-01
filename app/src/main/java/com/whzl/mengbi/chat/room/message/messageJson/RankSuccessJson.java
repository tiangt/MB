package com.whzl.mengbi.chat.room.message.messageJson;

import java.util.List;

/**
 * @author nobody
 * @date 2019-08-01
 */
public class RankSuccessJson {


    /**
     * context : {"sendUserId":30000808,"sendUserNickName":"国际级","sendUserLastUpdateTime":1556532042913,"nickName":"小猪佩奇就是我啊","userId":30000139,"lastUpdateTime":1559562373024,"animation":{"res":[{"resType":"https://test-img.mengbitv.com/default/000/00/05/68.svga","resUrl":"SVGA"}],"params":[{"paramsKey":"loopcount","paramsValue":"1"},{"paramsKey":"second","paramsValue":"5"}],"animationType":"SVGA"}}
     */

    public ContextBean context;

    public static class ContextBean {
        /**
         * sendUserId : 30000808
         * sendUserNickName : 国际级
         * sendUserLastUpdateTime : 1556532042913
         * nickName : 小猪佩奇就是我啊
         * userId : 30000139
         * lastUpdateTime : 1559562373024
         * animation : {"res":[{"resType":"https://test-img.mengbitv.com/default/000/00/05/68.svga","resUrl":"SVGA"}],"params":[{"paramsKey":"loopcount","paramsValue":"1"},{"paramsKey":"second","paramsValue":"5"}],"animationType":"SVGA"}
         */

        public int sendUserId;
        public String sendUserNickName;
        public long sendUserLastUpdateTime;
        public String nickName;
        public int userId;
        public long lastUpdateTime;
        public AnimationBean animation;

        public static class AnimationBean {
            /**
             * res : [{"resType":"https://test-img.mengbitv.com/default/000/00/05/68.svga","resUrl":"SVGA"}]
             * params : [{"paramsKey":"loopcount","paramsValue":"1"},{"paramsKey":"second","paramsValue":"5"}]
             * animationType : SVGA
             */

            public String animationType;
            public List<ResBean> res;
            public List<ParamsBean> params;

            public static class ResBean {
                /**
                 * resType : https://test-img.mengbitv.com/default/000/00/05/68.svga
                 * resUrl : SVGA
                 */

                public String resType;
                public String resUrl;
            }

            public static class ParamsBean {
                /**
                 * paramsKey : loopcount
                 * paramsValue : 1
                 */

                public String paramsKey;
                public String paramsValue;
            }
        }
    }
}
