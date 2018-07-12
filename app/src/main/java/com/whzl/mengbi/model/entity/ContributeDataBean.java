package com.whzl.mengbi.model.entity;

import java.util.List;

/**
 * @author shaw
 * @date 2018/7/12
 */
public class ContributeDataBean {

    /**
     * code : 200
     * msg : success
     * data : {"day":[{"userId":30000254,"nickname":"octerye","gender":0,"avatar":"http://dev.img.mengbitv.com/avatar/030/00/02/54.jpg?1528199262","level":24,"royalLevel":4,"value":1225800},{"userId":30000146,"nickname":"哈哈","gender":"","avatar":"http://dev.img.mengbitv.com/avatar/030/00/01/46.jpg?1529054979","level":15,"royalLevel":3,"value":158400}]}
     */

    public int code;
    public String msg;
    public DataBean data;

    public class DataBean {
        public List<UserInfoBean> day;
        public List<UserInfoBean> week;

        public class UserInfoBean {
            /**
             * userId : 30000254
             * nickname : octerye
             * gender : 0
             * avatar : http://dev.img.mengbitv.com/avatar/030/00/02/54.jpg?1528199262
             * level : 24
             * royalLevel : 4
             * value : 1225800
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
