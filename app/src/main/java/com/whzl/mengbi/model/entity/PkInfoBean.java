package com.whzl.mengbi.model.entity;

/**
 * @author nobody
 * @date 2018/10/8
 */
public class PkInfoBean {

    /**
     * launchUserInfo : {"userId":30000148,"nickname":"土豪30000148","avatar":"https://test-img.mengbitv.com/avatar/030/00/01/48_100x100.jpg?1529054800"}
     * launchPkUserProgramId : 100121
     * launchPkUserScore : 6
     * pkUserInfo : {"userId":30000139,"nickname":"路奇","avatar":"https://test-img.mengbitv.com/avatar/030/00/01/39_100x100.jpg?1537327157"}
     * pkUserProgramId : 100079
     * pkUserScore : 5
     * pkStatus : T
     * punishStatus : F
     * tieStatus : F
     * pkSurPlusSecond : 264
     * punishSurPlusSecond : 0
     * tieSurPlusSecond : 0
     */

    public userInfoBean launchUserInfo;
    public int launchPkUserProgramId;
    public int launchPkUserScore;
    public userInfoBean pkUserInfo;
    public int pkUserProgramId;
    public int pkUserScore;
    public String pkStatus;
    public String punishStatus;
    public String tieStatus;
    public int pkSurPlusSecond;
    public int punishSurPlusSecond;
    public int tieSurPlusSecond;

    public static class userInfoBean {
        /**
         * userId : 30000148
         * nickname : 土豪30000148
         * avatar : https://test-img.mengbitv.com/avatar/030/00/01/48_100x100.jpg?1529054800
         */

        public int userId;
        public String nickname;
        public String avatar;
    }
}
