package com.whzl.mengbi.model.entity;

import java.util.List;

/**
 * @author nobody
 * @date 2019/4/17
 */
public class RoomAnnouceBean {

    public List<ListBean> list;

    public static class ListBean {
        /**
         * name : 哈呵呵
         * content : 全部 全部 号外 号外 测试消息推送！！！
         * picUrl : https://test-img.mengbitv.com/default/000/00/02/03.jpg
         * contentLink :
         */

        public String name;
        public String content;
        public String picUrl;
        public String contentLink;
    }
}
