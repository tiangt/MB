package com.whzl.mengbi.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @author cliang
 * @date 2018.12.4
 */
public class PersonalInfoBean {

    /**
     * code : 200
     * msg : success
     * data : {"userId":30000254,"userType":"MANAGER","nickname":"sdfsdfs/","avatar":"http://test-img.mengbitv.com/avatar/030/00/02/54_100x100.jpg?1542176334","introduce":"~~~主人很懒，什么都没有留下~~~r","birthday":"2018-11-14 00:00:00","province":"浙江省","city":"杭州市","levelList":[{"levelType":"ROYAL_LEVEL","levelValue":0,"levelName":"平民","expList":[{"expType":"ROYAL_EXP","expName":"贵族经验","sjExpvalue":0,"bjExpValue":0,"sjNeedExpValue":200,"bjNeedExpValue":0}]},{"levelType":"USER_LEVEL","levelValue":26,"levelName":"公爵","expList":[{"expType":"USER_EXP","expName":"用户经验","sjExpvalue":44628611,"bjExpValue":44628611,"sjNeedExpValue":50000000,"bjNeedExpValue":0}]}],"goodsList":[{"goodsId":300,"goodsName":"守护","goodsType":"GUARD","goodsIcon":"http://test-img.mengbitv.com/default/000/00/01/39.jpg","bindProgramId":100079},{"goodsId":300,"goodsName":"守护","goodsType":"GUARD","goodsIcon":"http://test-img.mengbitv.com/default/000/00/01/39.jpg","bindProgramId":100140},{"goodsId":300,"goodsName":"守护","goodsType":"GUARD","goodsIcon":"http://test-img.mengbitv.com/default/000/00/01/39.jpg","bindProgramId":100147},{"goodsId":301,"goodsName":"vip","goodsType":"VIP","goodsIcon":"http://test-img.mengbitv.com/default/000/00/03/29.jpg","bindProgramId":0},{"goodsId":94342,"goodsName":"奔驰SLK","goodsType":"CAR","goodsIcon":"http://test-img.mengbitv.com/default/000/00/01/71.jpg","bindProgramId":0},{"goodsId":94374,"goodsName":"富豪榜周榜第6名","goodsType":"BADGE","goodsIcon":"http://test-img.mengbitv.com/default/000/00/02/61.jpg","bindProgramId":0},{"goodsId":94538,"goodsName":"3432343","goodsType":"PRETTY_NUM","goodsIcon":"http://test-img.mengbitv.com/default/000/00/00/00.jpg","bindProgramId":0}],"weathMap":{"coin":3814062,"point":0},"rank":16,"isFollowed":"F","liveStatus":"F","fansNum":0,"myFollowNum":0}
     */

    private int code;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean implements Parcelable {
        /**
         * userId : 30000254
         * userType : MANAGER
         * nickname : sdfsdfs/
         * avatar : http://test-img.mengbitv.com/avatar/030/00/02/54_100x100.jpg?1542176334
         * introduce : ~~~主人很懒，什么都没有留下~~~r
         * birthday : 2018-11-14 00:00:00
         * province : 浙江省
         * city : 杭州市
         * levelList : [{"levelType":"ROYAL_LEVEL","levelValue":0,"levelName":"平民","expList":[{"expType":"ROYAL_EXP","expName":"贵族经验","sjExpvalue":0,"bjExpValue":0,"sjNeedExpValue":200,"bjNeedExpValue":0}]},{"levelType":"USER_LEVEL","levelValue":26,"levelName":"公爵","expList":[{"expType":"USER_EXP","expName":"用户经验","sjExpvalue":44628611,"bjExpValue":44628611,"sjNeedExpValue":50000000,"bjNeedExpValue":0}]}]
         * goodsList : [{"goodsId":300,"goodsName":"守护","goodsType":"GUARD","goodsIcon":"http://test-img.mengbitv.com/default/000/00/01/39.jpg","bindProgramId":100079},{"goodsId":300,"goodsName":"守护","goodsType":"GUARD","goodsIcon":"http://test-img.mengbitv.com/default/000/00/01/39.jpg","bindProgramId":100140},{"goodsId":300,"goodsName":"守护","goodsType":"GUARD","goodsIcon":"http://test-img.mengbitv.com/default/000/00/01/39.jpg","bindProgramId":100147},{"goodsId":301,"goodsName":"vip","goodsType":"VIP","goodsIcon":"http://test-img.mengbitv.com/default/000/00/03/29.jpg","bindProgramId":0},{"goodsId":94342,"goodsName":"奔驰SLK","goodsType":"CAR","goodsIcon":"http://test-img.mengbitv.com/default/000/00/01/71.jpg","bindProgramId":0},{"goodsId":94374,"goodsName":"富豪榜周榜第6名","goodsType":"BADGE","goodsIcon":"http://test-img.mengbitv.com/default/000/00/02/61.jpg","bindProgramId":0},{"goodsId":94538,"goodsName":"3432343","goodsType":"PRETTY_NUM","goodsIcon":"http://test-img.mengbitv.com/default/000/00/00/00.jpg","bindProgramId":0}]
         * weathMap : {"coin":3814062,"point":0}
         * rank : 16
         * isFollowed : F
         * liveStatus : F
         * fansNum : 0
         * myFollowNum : 0
         */

        private int userId;
        private String userType;
        private String nickname;
        private String avatar;
        private String introduce;
        private String birthday;
        private String province;
        private String city;
        private WeathMapBean weathMap;
        private int rank;
        private String isFollowed;
        private String liveStatus;
        private int fansNum;
        private int myFollowNum;
        private List<LevelListBean> levelList;
        private List<GoodsListBean> goodsList;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public String getIntroduce() {
            return introduce;
        }

        public void setIntroduce(String introduce) {
            this.introduce = introduce;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getProvince() {
            return province;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public WeathMapBean getWeathMap() {
            return weathMap;
        }

        public void setWeathMap(WeathMapBean weathMap) {
            this.weathMap = weathMap;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }

        public String getIsFollowed() {
            return isFollowed;
        }

        public void setIsFollowed(String isFollowed) {
            this.isFollowed = isFollowed;
        }

        public String getLiveStatus() {
            return liveStatus;
        }

        public void setLiveStatus(String liveStatus) {
            this.liveStatus = liveStatus;
        }

        public int getFansNum() {
            return fansNum;
        }

        public void setFansNum(int fansNum) {
            this.fansNum = fansNum;
        }

        public int getMyFollowNum() {
            return myFollowNum;
        }

        public void setMyFollowNum(int myFollowNum) {
            this.myFollowNum = myFollowNum;
        }

        public List<LevelListBean> getLevelList() {
            return levelList;
        }

        public void setLevelList(List<LevelListBean> levelList) {
            this.levelList = levelList;
        }

        public List<GoodsListBean> getGoodsList() {
            return goodsList;
        }

        public void setGoodsList(List<GoodsListBean> goodsList) {
            this.goodsList = goodsList;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {

        }

        public static class WeathMapBean {
            /**
             * coin : 3814062
             * point : 0
             */

            private long coin;
            private int point;

            public long getCoin() {
                return coin;
            }

            public void setCoin(int coin) {
                this.coin = coin;
            }

            public int getPoint() {
                return point;
            }

            public void setPoint(int point) {
                this.point = point;
            }
        }

        public static class LevelListBean {
            /**
             * levelType : ROYAL_LEVEL
             * levelValue : 0
             * levelName : 平民
             * expList : [{"expType":"ROYAL_EXP","expName":"贵族经验","sjExpvalue":0,"bjExpValue":0,"sjNeedExpValue":200,"bjNeedExpValue":0}]
             */

            private String levelType;
            private int levelValue;
            private String levelName;
            private List<ExpListBean> expList;

            public String getLevelType() {
                return levelType;
            }

            public void setLevelType(String levelType) {
                this.levelType = levelType;
            }

            public int getLevelValue() {
                return levelValue;
            }

            public void setLevelValue(int levelValue) {
                this.levelValue = levelValue;
            }

            public String getLevelName() {
                return levelName;
            }

            public void setLevelName(String levelName) {
                this.levelName = levelName;
            }

            public List<ExpListBean> getExpList() {
                return expList;
            }

            public void setExpList(List<ExpListBean> expList) {
                this.expList = expList;
            }

            public static class ExpListBean {
                /**
                 * expType : ROYAL_EXP
                 * expName : 贵族经验
                 * sjExpvalue : 0
                 * bjExpValue : 0
                 * sjNeedExpValue : 200
                 * bjNeedExpValue : 0
                 */

                private String expType;
                private String expName;
                private long sjExpvalue;
                private long bjExpValue;
                private long sjNeedExpValue;
                private long bjNeedExpValue;

                public String getExpType() {
                    return expType;
                }

                public void setExpType(String expType) {
                    this.expType = expType;
                }

                public String getExpName() {
                    return expName;
                }

                public void setExpName(String expName) {
                    this.expName = expName;
                }

                public long getSjExpvalue() {
                    return sjExpvalue;
                }

                public void setSjExpvalue(int sjExpvalue) {
                    this.sjExpvalue = sjExpvalue;
                }

                public long getBjExpValue() {
                    return bjExpValue;
                }

                public void setBjExpValue(int bjExpValue) {
                    this.bjExpValue = bjExpValue;
                }

                public long getSjNeedExpValue() {
                    return sjNeedExpValue;
                }

                public void setSjNeedExpValue(int sjNeedExpValue) {
                    this.sjNeedExpValue = sjNeedExpValue;
                }

                public long getBjNeedExpValue() {
                    return bjNeedExpValue;
                }

                public void setBjNeedExpValue(int bjNeedExpValue) {
                    this.bjNeedExpValue = bjNeedExpValue;
                }
            }
        }

        public static class GoodsListBean {
            /**
             * goodsId : 300
             * goodsName : 守护
             * goodsType : GUARD
             * goodsIcon : http://test-img.mengbitv.com/default/000/00/01/39.jpg
             * bindProgramId : 100079
             * "goodsColor":"A"
             */

            private int goodsId;
            private String goodsName;
            private String goodsType;
            private String goodsIcon;
            private int bindProgramId;
            private String goodsColor;

            public String getGoodsColor() {
                return goodsColor;
            }

            public void setGoodsColor(String goodsColor) {
                this.goodsColor = goodsColor;
            }

            public int getGoodsId() {
                return goodsId;
            }

            public void setGoodsId(int goodsId) {
                this.goodsId = goodsId;
            }

            public String getGoodsName() {
                return goodsName;
            }

            public void setGoodsName(String goodsName) {
                this.goodsName = goodsName;
            }

            public String getGoodsType() {
                return goodsType;
            }

            public void setGoodsType(String goodsType) {
                this.goodsType = goodsType;
            }

            public String getGoodsIcon() {
                return goodsIcon;
            }

            public void setGoodsIcon(String goodsIcon) {
                this.goodsIcon = goodsIcon;
            }

            public int getBindProgramId() {
                return bindProgramId;
            }

            public void setBindProgramId(int bindProgramId) {
                this.bindProgramId = bindProgramId;
            }
        }
    }
}
