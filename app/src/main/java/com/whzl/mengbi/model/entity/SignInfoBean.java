package com.whzl.mengbi.model.entity;

import java.util.List;

/**
 * @author nobody
 * @date 2019/5/8
 */
public class SignInfoBean {

    /**
     * list : [{"awardName":"星期一签到,1000用户经验","awardId":23190,"totalSecond":null,"needCompletion":1,"completion":0,"picUrl":"https://localtest-img.mengbitv.com/app-img/activity/sign/23190.png","signStatus":"retroactive","awardGoods":"1000用户经验","dayIndex":1},{"awardName":"星期二签到,5个蓝色妖姬","awardId":23191,"totalSecond":null,"needCompletion":1,"completion":0,"picUrl":"https://localtest-img.mengbitv.com/app-img/activity/sign/23191.png","signStatus":"retroactive","awardGoods":"5个蓝色妖姬","dayIndex":2},{"awardName":"期三签到,2000用户经验","awardId":23192,"totalSecond":null,"needCompletion":1,"completion":1,"picUrl":"https://localtest-img.mengbitv.com/app-img/activity/sign/23192.png","signStatus":"signed","awardGoods":"2000用户经验","dayIndex":3},{"awardName":"星期四签到,10个蓝色妖姬","awardId":23193,"totalSecond":null,"needCompletion":1,"completion":0,"picUrl":"https://localtest-img.mengbitv.com/app-img/activity/sign/23193.png","signStatus":"notsign","awardGoods":"10个蓝色妖姬","dayIndex":4},{"awardName":"星期五签到,签到之星勋章7天","awardId":23194,"totalSecond":null,"needCompletion":1,"completion":0,"picUrl":"https://localtest-img.mengbitv.com/app-img/activity/sign/23194.png","signStatus":"notsign","awardGoods":"签到之星勋章","dayIndex":5},{"awardName":"星六签到,1个广播卡 有效期7天","awardId":23195,"totalSecond":null,"needCompletion":1,"completion":0,"picUrl":"https://localtest-img.mengbitv.com/app-img/activity/sign/23195.png","signStatus":"notsign","awardGoods":"一个广播卡","dayIndex":6},{"awardName":"星期天签到,1个改名卡 有效期7天","awardId":23196,"totalSecond":null,"needCompletion":1,"completion":0,"picUrl":"https://localtest-img.mengbitv.com/app-img/activity/sign/23196.png","signStatus":"notsign","awardGoods":"一个改名卡","dayIndex":7},{"awardName":"连续7天奖励","awardId":23197,"totalSecond":null,"needCompletion":7,"completion":0,"picUrl":"https://localtest-img.mengbitv.com/app-img/activity/sign/23197.png","signStatus":"notsign","awardGoods":"神秘全勤奖","dayIndex":7}]
     * curAwardId : 23192
     */

    public int curAwardId;
    public List<ListBean> list;

    public static class ListBean {
        /**
         * awardName : 星期一签到,1000用户经验
         * awardId : 23190
         * totalSecond : null
         * needCompletion : 1
         * completion : 0
         * picUrl : https://localtest-img.mengbitv.com/app-img/activity/sign/23190.png
         * signStatus : retroactive
         * awardGoods : 1000用户经验
         * dayIndex : 1
         */

        public String awardName;
        public int awardId;
        public Object totalSecond;
        public int needCompletion;
        public int completion;
        public String picUrl;
        public String signStatus;
        public String awardGoods;
        public int dayIndex;
    }
}
