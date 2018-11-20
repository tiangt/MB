package com.whzl.mengbi.model.entity;

import com.whzl.mengbi.chat.room.message.messageJson.RunWayJson;

import java.util.List;

/**
 * @author shaw
 * @date 2018/8/2
 */
public class RunWayListBean {

    public List<RunwayBean.ContextEntity> list;

    public class ContextEntity {
        /**
         * userId : 30000254
         * toUserId : 30000139
         * nickname : octerye
         * toNickname : 哈哈哈看哈哈哈哈哈哈
         * count : 1
         * goodsName : 花美人
         * goodsPic : http://test-img.mengbitv.com/default/000/00/01/12.jpg
         * cacheIt : true
         * seconds : 57
         * dateLong : 1532943409984
         * programId : 100079
         * runwayType:跑道类型（destory 攻占标志、getOn 登上标志）
         */

        public int userId;
        public int toUserId;
        public String nickname;
        public String toNickname;
        public int count;
        public String goodsName;
        public String goodsPic;
        public boolean cacheIt;
        public int seconds;
        public long dateLong;
        public int programId;
        public String runwayType;
    }
}
