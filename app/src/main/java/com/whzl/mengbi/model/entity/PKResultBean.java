package com.whzl.mengbi.model.entity;

import java.util.List;

/**
 * @author cliang
 * @date 2018.11.13
 */
public class PKResultBean {

    /**
     * launchUserInfo : {"userId":30000012,"nickname":"修改昵称","avatar":"http://test-img.mengbitv.com/avatar/030/00/00/12_100x100.jpg?1529897703"}
     * launchPkUserProgramId : 100001
     * launchPkUserScore : 1
     * launchPkUserFans : [{"userId":30000607,"nickname":"萌友30000607","avatar":"http://test-img.mengbitv.com/avatar/030/00/06/07_100x100.jpg?1532419849","score":1}]
     * pkUserInfo : {"userId":30000607,"nickname":"萌友30000607","avatar":"http://test-img.mengbitv.com/avatar/030/00/06/07_100x100.jpg?1532419849"}
     * pkUserProgramId : 100148
     * pkUserScore : 1
     * pkUserFans : [{"userId":30000607,"nickname":"萌友30000607","avatar":"http://test-img.mengbitv.com/avatar/030/00/06/07_100x100.jpg?1532419849","score":1}]
     * pkStatus : F
     * punishStatus : T
     * tieStatus : F
     * pkSurPlusSecond : 0
     * punishSurPlusSecond : 58
     * tieSurPlusSecond : 0
     * mvpUser : {"userId":30000607,"nickname":"萌友30000607","avatar":"http://test-img.mengbitv.com/avatar/030/00/06/07_100x100.jpg?1532419849"}
     * punishWay : S蹲
     * otherStream : {"rtmp":["rtmp://livedown.mengbitv.com/live/100148"],"flv":["http://livedown.mengbitv.com/live/100148.flv"],"hls":["http://livedown.mengbitv.com/live/100148/playlist.m3u8"]}
     */

    public UserInfoBean launchUserInfo;
    public int launchPkUserProgramId;
    public int launchPkUserScore;
    public UserInfoBean pkUserInfo;
    public int pkUserProgramId;
    public int pkUserScore;
    public String pkStatus;
    public String punishStatus;
    public String tieStatus;
    public int pkSurPlusSecond;
    public int punishSurPlusSecond;
    public int tieSurPlusSecond;
    public MvpUserBean mvpUser;
    public String punishWay;
    public OtherStreamBean otherStream;
    public List<PKFansBean> launchPkUserFans;
    public List<PKFansBean> pkUserFans;

    public static class UserInfoBean {
        /**
         * userId : 30000607
         * nickname : 萌友30000607
         * avatar : http://test-img.mengbitv.com/avatar/030/00/06/07_100x100.jpg?1532419849
         */

        public int userId;
        public String nickname;
        public String avatar;
    }

    public static class MvpUserBean {
        /**
         * userId : 30000607
         * nickname : 萌友30000607
         * avatar : http://test-img.mengbitv.com/avatar/030/00/06/07_100x100.jpg?1532419849
         */

        public int userId;
        public String nickname;
        public String avatar;
    }

    public static class OtherStreamBean {
        private List<String> rtmp;
        private List<String> flv;
        private List<String> hls;

        public List<String> getRtmp() {
            return rtmp;
        }

        public void setRtmp(List<String> rtmp) {
            this.rtmp = rtmp;
        }

        public List<String> getFlv() {
            return flv;
        }

        public void setFlv(List<String> flv) {
            this.flv = flv;
        }

        public List<String> getHls() {
            return hls;
        }

        public void setHls(List<String> hls) {
            this.hls = hls;
        }
    }


}

