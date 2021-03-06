package com.whzl.mengbi.model.entity;

import java.util.List;


public class RankListInfo {

    public int code;
    public String msg;
    public DataBean data;

    public static class DataBean {
        public List<ListBean> list;

        public static class ListBean {

            public UserBean user;
            public RankBean rank;
            public ProgramBean program;

            public static class UserBean {

                public int userId;
                public String nickname;
                public String avatar;
                public String levelType;
                public int level;
            }

            public static class RankBean {
                public long ranking;
                public long score;
                public long gap;
            }

            public static class ProgramBean {
                public int programId;
                public String status;
            }
        }
    }
}
