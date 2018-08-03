package com.whzl.mengbi.model;

import java.util.ArrayList;

/**
 * @author shaw
 * @date 2018/8/3
 */
public class GuardListBean {

    public ArrayList<GuardDetailBean> list;

    public class GuardDetailBean {
        /**
         * programId : 100002
         * userId : 30000139
         * nickName : 哈哈哈哈哈哈哈哈和哈
         * picId : -1
         * isOnline : 1
         * guardGoodsId : 300
         * rankingsValue : 0
         * lastUpdateTime : 2018-06-06 12:10:20
         * userLevel : 24
         * surplusDays : 90
         * colorSpeak : false
         * avatar : http://dev.img.mengbitv.com/avatar/030/00/01/39.jpg?1528258220
         */

        public int programId;
        public int userId;
        public String nickName;
        public String picId;
        public int isOnline;
        public int guardGoodsId;
        public int rankingsValue;
        public String lastUpdateTime;
        public int userLevel;
        public int surplusDays;
        public boolean colorSpeak;
        public String avatar;
    }
}
