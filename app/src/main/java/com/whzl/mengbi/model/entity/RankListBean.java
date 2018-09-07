package com.whzl.mengbi.model.entity;

import java.util.ArrayList;

/**
 * @author shaw
 * @date 2018/8/22
 */
public class RankListBean {

    public ArrayList<DetailBean> list;

    public class DetailBean {


        public UserBean user;
        public RankBean rank;
        public ProgramBean program;

        public class UserBean {

            public int userId;
            public String nickname;
            public String avatar;
            public int level;
            public String levelType;
        }

        public class RankBean {
            public long ranking;
            public int score;
            public long gap;
        }

        public class ProgramBean {

            public int programId;
            public String status;
        }
    }
}
