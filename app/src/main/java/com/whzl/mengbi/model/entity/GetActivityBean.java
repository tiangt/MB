package com.whzl.mengbi.model.entity;

import java.util.List;

/**
 * @author nobody
 * @date 2018/9/30
 */
public class GetActivityBean {

    public List<ListBean> list;

    public static class ListBean {
        /**
         * id : 836
         * name : 幸运抽奖
         * linkUrl : https://test.mengbitb.com/spread/rand
         * imageUrl : https://test-img.mengbitv.com/default/000/00/03/14.jpg
         */

        public int id;
        public String name;
        public String linkUrl;
        public String imageUrl;
        public String flag;
    }
}
