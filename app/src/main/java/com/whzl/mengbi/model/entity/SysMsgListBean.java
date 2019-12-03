package com.whzl.mengbi.model.entity;

import java.util.List;

/**
 * @author nobody
 * @date 2019-11-27
 */
public class SysMsgListBean {

    public List<ListBean> list;

    public static class ListBean {
        /**
         * title : 部分用户
         * content : 部分用户
         * contentType : text
         * contentLink :
         * systemMessageId : 8
         * expireTime : 2019-11-23 10:05:15
         * remindId : 4311
         * messageType : SYSTEM_MESSAGE
         * isRead : F
         * userId : 30000150
         */

        public String title;
        public String content;
        public String contentType;
        public String contentLink;
        public int systemMessageId;
        public String expireTime;
        public String createTime;
        public int remindId;
        public String messageType;
        public String isRead;
        public int userId;
    }
}
