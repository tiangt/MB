package com.whzl.mengbi.model.entity;

import java.util.List;

/**
 * @author nobody
 * @date 2019-06-18
 */
public class FlopAwardRecordBean {

    public List<ListBean> list;

    public static class ListBean {
        /**
         * type : GOODS
         * typeName :
         * name : 对不起
         * num : 2
         * price : 100000
         * pic : http://localtest-img.mengbitv.com/default/000/00/04/56.jpg
         * index : 0
         * nickName : 好咯破老头
         */

        public String type;
        public String typeName;
        public String name;
        public int num;
        public int price;
        public String pic;
        public int index;
        public String nickName;
    }
}
