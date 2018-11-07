package com.whzl.mengbi.model.entity;

import java.util.List;

/**
 * @author cliang
 * @date 2018.11.7
 */
public class RoomRankBean {

    /**
     * code : 200
     * msg : success
     * data : {"list":[{"userId":30000142,"nickname":"30000142","gender":"","avatar":"http://test-img.mengbitv.com/avatar/030/00/01/42_100x100.jpg?1537262893","level":29,"royalLevel":0,"value":18100200},{"userId":30000530,"nickname":"呵呵嗒","gender":0,"avatar":"http://test-img.mengbitv.com/avatar/030/00/05/30_100x100.jpg?1539134765","level":30,"royalLevel":0,"value":15768322},{"userId":30000711,"nickname":"种地","gender":"M","avatar":"http://test-img.mengbitv.com/avatar/030/00/07/11_100x100.jpg?1539934410","level":23,"royalLevel":0,"value":7348380},{"userId":30000718,"nickname":"萌友30000718","gender":0,"avatar":"http://test-img.mengbitv.com/avatar/030/00/07/18_100x100.jpg?1538120824","level":14,"royalLevel":4,"value":3403488},{"userId":30000139,"nickname":"路奇","gender":"","avatar":"http://test-img.mengbitv.com/avatar/030/00/01/39_100x100.jpg?1537327157","level":29,"royalLevel":6,"value":2866099},{"userId":30000162,"nickname":"哈娜哈娜哈娜哈娜哈娜","gender":"W","avatar":"http://test-img.mengbitv.com/avatar/030/00/01/62_100x100.jpg?1539587632","level":18,"royalLevel":7,"value":1300000},{"userId":30000517,"nickname":"萌友30000517","gender":0,"avatar":"http://test-img.mengbitv.com/avatar/030/00/05/17_100x100.jpg?1528795360","level":28,"royalLevel":6,"value":1158300},{"userId":30000580,"nickname":"萌友30","gender":"W","avatar":"http://test-img.mengbitv.com/avatar/030/00/05/80_100x100.jpg?1534327031","level":34,"royalLevel":0,"value":382500}]}
     */

    public int code;
    public String msg;
    public DataBean data;

    public class DataBean {
        public List<ListBean> list;

        public class ListBean {
            /**
             * userId : 30000142
             * nickname : 30000142
             * gender :
             * avatar : http://test-img.mengbitv.com/avatar/030/00/01/42_100x100.jpg?1537262893
             * level : 29
             * royalLevel : 0
             * value : 18100200
             */

            public int userId;
            public String nickname;
            public String gender;
            public String avatar;
            public int level;
            public int royalLevel;
            public int value;

        }
    }
}
