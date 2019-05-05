package com.whzl.mengbi.model.entity;

import java.util.List;

/**
 * @author nobody
 * @date 2019/4/25
 */
public class QuickChannelBean {


    public List<ChannelListBean> channelList;
    public List<RuleListBean> ruleList;

    public static class ChannelListBean {
        /**
         * channelId : 40
         * channelName : 支付宝手机支付
         * channelFlag : AlipayMobile
         * status : ACTIVE
         * platform : MOBILE
         * pic : https://test-img.mengbitv.com/default/000/08/93/42.jpg
         * sortValue : 0
         */

        public int channelId;
        public String channelName;
        public String channelFlag;
        public String status;
        public String platform;
        public String pic;
        public int sortValue;
    }

    public static class RuleListBean {
        /**
         * chargeCount : 10000
         * chengCount : 100000
         * list : [{"ruleId":483,"rechargeCountCode":"","type":"ALIPAY_M_RMB","name":"人民币","channelCat":"currency","channelFlag":"AlipayMobile"},{"ruleId":484,"rechargeCountCode":"","type":"WEIXIN_APP_RMB","name":"人民币","channelCat":"currency","channelFlag":"WeixinApp"}]
         */

        public int chargeCount;
        public int chengCount;
        public List<ListBean> list;

        public static class ListBean {
            /**
             * ruleId : 483
             * rechargeCountCode :
             * type : ALIPAY_M_RMB
             * name : 人民币
             * channelCat : currency
             * channelFlag : AlipayMobile
             */

            public int ruleId;
            public String rechargeCountCode;
            public String type;
            public String name;
            public String channelCat;
            public String channelFlag;
        }
    }
}
