package com.whzl.mengbi.model.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author shaw
 * @date 2018/9/3
 */
public class TreasureBoxStatusBean {

    public ArrayList<ListBean> list;

    public class ListBean {
        /**
         * taskId : 6
         * maxOnlineTimes : 0
         * maxUngrandAwardTimes : 1
         */

        public int taskId;
        public int maxOnlineTimes;
        public int maxUngrandAwardTimes;
    }
}
