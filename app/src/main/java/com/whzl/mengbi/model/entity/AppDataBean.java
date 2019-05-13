package com.whzl.mengbi.model.entity;

/**
 * @author shaw
 * @date 2018/8/24
 */
public class AppDataBean {
    public String imgHost;
    public String userProtocolUrl;
    public String redpacketUrl;
    public String weekStarHelpUrl;
    public String anchorAgreeUrl;
    public String userAgreeUrl;
    public String userGradeUrl;
    public String anchorGradeUrl;
    public String redpacketHelpUrl;
    public UserAwardBean newUserAward;

    public class UserAwardBean {
        public String guestUserAward;
        public String loginUserAward;
    }
}
