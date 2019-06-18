package com.whzl.mengbi.model.entity;

import java.util.List;

/**
 * @author nobody
 * @date 2019-06-18
 */
public class StartFlopBean {

    public List<ListBean> list;

    public static class ListBean {
        /**
         * type : GOODS
         * typeName :
         * name : 666
         * num : 300
         * price : 30000
         * pic : http://localtest-img.mengbitv.com/default/000/00/03/36.jpg
         * index : 0
         */

        public String type;
        public String typeName;
        public String name;
        public int num;
        public int price;
        public String pic;
        public int index;
    }
}
