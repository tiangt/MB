package com.whzl.mengbi.model.entity;

import java.util.List;

/**
 * @author shaw
 * @date 2018/7/10
 */
public class RoomUserInfo {

    /**
     * code : 200
     * msg : success
     * data : {"userId":30000139,"userType":"ANCHOR","nickname":"哈哈哈哈哈哈哈哈和哈","avatar":"http://dev.img.mengbitv.com/avatar/030/00/01/39.jpg?1528258220","status":"ACTIVE","sex":"","introduce":"php echo ？","birthday":"2018-02-22 00:00:00","province":"浙江省","city":"杭州市","levelMap":[{"levelType":"ANCHOR_LEVEL","levelValue":49,"levelName":"天使9","expList":[{"expType":"GIFT_EXP","expName":"付费礼物经验","sjExpvalue":2976859652,"bjExpValue":2976859652,"sjNeedExpValue":3000000000,"bjNeedExpValue":0}]},{"levelType":"ROYAL_LEVEL","levelValue":0,"levelName":"平民","expList":[{"expType":"ROYAL_EXP","expName":"贵族经验","sjExpvalue":0,"bjExpValue":0,"sjNeedExpValue":200,"bjNeedExpValue":0}]},{"levelType":"USER_LEVEL","levelValue":24,"levelName":"伯爵","expList":[{"expType":"USER_EXP","expName":"用户经验","sjExpvalue":20888600,"bjExpValue":20888600,"sjNeedExpValue":30000000,"bjNeedExpValue":0}]}],"isSubs":true,"identityId":10,"isVip":false,"goodsList":[{"goodsId":300,"goodsName":"守护","goodsType":"GUARD","goodsIcon":1005,"bindProgramId":100079,"colorSpeak":true}],"weathMap":{"coin":79367423,"chengPonit":1846429640}}
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

    public static class DataBean {

        private int userId;
        private String userType;
        private String nickname;
        private String avatar;
        private String status;
        private String sex;
        private String introduce;
        private String birthday;
        private String province;
        private String city;
        private boolean isSubs;
        private int identityId;
        private boolean isVip;
        private WeathMapBean weathMap;
        private List<LevelMapBean> levelMap;
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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
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

        public boolean isIsSubs() {
            return isSubs;
        }

        public void setIsSubs(boolean isSubs) {
            this.isSubs = isSubs;
        }

        public int getIdentityId() {
            return identityId;
        }

        public void setIdentityId(int identityId) {
            this.identityId = identityId;
        }

        public boolean isIsVip() {
            return isVip;
        }

        public void setIsVip(boolean isVip) {
            this.isVip = isVip;
        }

        public WeathMapBean getWeathMap() {
            return weathMap;
        }

        public void setWeathMap(WeathMapBean weathMap) {
            this.weathMap = weathMap;
        }

        public List<LevelMapBean> getLevelMap() {
            return levelMap;
        }

        public void setLevelMap(List<LevelMapBean> levelMap) {
            this.levelMap = levelMap;
        }

        public List<GoodsListBean> getGoodsList() {
            return goodsList;
        }

        public void setGoodsList(List<GoodsListBean> goodsList) {
            this.goodsList = goodsList;
        }

        public static class WeathMapBean {
            /**
             * coin : 79367423
             * chengPonit : 1846429640
             */

            private int coin;
            private int chengPonit;

            public int getCoin() {
                return coin;
            }

            public void setCoin(int coin) {
                this.coin = coin;
            }

            public int getChengPonit() {
                return chengPonit;
            }

            public void setChengPonit(int chengPonit) {
                this.chengPonit = chengPonit;
            }
        }

        public static class LevelMapBean {

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
                 * expType : GIFT_EXP
                 * expName : 付费礼物经验
                 * sjExpvalue : 2976859652
                 * bjExpValue : 2976859652
                 * sjNeedExpValue : 3000000000
                 * bjNeedExpValue : 0
                 */

                private String expType;
                private String expName;
                private long sjExpvalue;
                private long bjExpValue;
                private long sjNeedExpValue;
                private int bjNeedExpValue;

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

                public void setSjExpvalue(long sjExpvalue) {
                    this.sjExpvalue = sjExpvalue;
                }

                public long getBjExpValue() {
                    return bjExpValue;
                }

                public void setBjExpValue(long bjExpValue) {
                    this.bjExpValue = bjExpValue;
                }

                public long getSjNeedExpValue() {
                    return sjNeedExpValue;
                }

                public void setSjNeedExpValue(long sjNeedExpValue) {
                    this.sjNeedExpValue = sjNeedExpValue;
                }

                public int getBjNeedExpValue() {
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
             * goodsIcon : 1005
             * bindProgramId : 100079
             * colorSpeak : true
             */

            private int goodsId;
            private String goodsName;
            private String goodsType;
            private String goodsIcon;
            private int bindProgramId;
            private boolean colorSpeak;

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

            public boolean isColorSpeak() {
                return colorSpeak;
            }

            public void setColorSpeak(boolean colorSpeak) {
                this.colorSpeak = colorSpeak;
            }
        }
    }
}
