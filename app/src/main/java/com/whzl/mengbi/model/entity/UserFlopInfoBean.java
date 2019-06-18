package com.whzl.mengbi.model.entity;

import java.util.List;

/**
 * @author nobody
 * @date 2019-06-18
 */
public class UserFlopInfoBean {


    /**
     * userLuckVal : -45
     * guessPrice : {"number":16000,"wealthType":"MENG_DOU"}
     * list : [{"type":"GOODS","typeName":"","name":"改名卡","num":1,"price":10000,"pic":"https://test-img.mengbitv.com/default/000/00/06/95.jpg","index":1},{"type":"GOODS","typeName":"","name":"小黄瓜","num":400,"price":40000,"pic":"https://test-img.mengbitv.com/default/000/00/03/38.jpg","index":0},{"type":"GOODS","typeName":"","name":"对不起","num":1,"price":50000,"pic":"https://test-img.mengbitv.com/default/000/00/04/56.jpg","index":0},{"type":"GOODS","typeName":"","name":"花美人","num":3,"price":150000,"pic":"https://test-img.mengbitv.com/default/000/00/03/64.jpg","index":0},{"type":"GOODS","typeName":"","name":"飞机","num":2,"price":100000,"pic":"https://test-img.mengbitv.com/default/000/00/03/48.jpg","index":0},{"type":"GOODS","typeName":"","name":"新年快乐","num":4,"price":200000,"pic":"https://test-img.mengbitv.com/default/000/00/05/09.jpg","index":0},{"type":"GOODS","typeName":"","name":"求婚","num":1,"price":520000,"pic":"https://test-img.mengbitv.com/default/000/00/05/37.jpg","index":0},{"type":"GOODS","typeName":"","name":"求婚","num":2,"price":1040000,"pic":"https://test-img.mengbitv.com/default/000/00/05/37.jpg","index":0},{"type":"WEALTH","typeName":"MENG_DOU","name":"萌豆","num":5000,"price":5000,"pic":"https://test-static.mengbitv.com/images/cardGame/card_game_logo_8.png","index":0}]
     */

    public int userLuckVal;
    public GuessPriceBean guessPrice;
    public List<ListBean> list;

    public static class GuessPriceBean {
        /**
         * number : 16000
         * wealthType : MENG_DOU
         */

        public int number;
        public String wealthType;
    }

    public static class ListBean {
        /**
         * type : GOODS
         * typeName :
         * name : 改名卡
         * num : 1
         * price : 10000
         * pic : https://test-img.mengbitv.com/default/000/00/06/95.jpg
         * index : 1
         */

        public String flag;
        public String type;
        public String typeName;
        public String name;
        public int num;
        public int price;
        public String pic;
        public int index;
        public int userLuckVal;
    }
}
