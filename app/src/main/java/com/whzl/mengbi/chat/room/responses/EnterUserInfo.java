package com.whzl.mengbi.chat.room.responses;

import java.util.List;


public class EnterUserInfo extends BaseResponse{

    /**
     * data : {"isVip":0,"nickname":"alex","introduce":"bhuhbbb","levelList":[{"levelType":"ROYAL_LEVEL","expList":[{"sjExpvalue":0,"bjExpValue":0,"expName":"贵族经验","sjNeedExpValue":200,"bjNeedExpValue":0,"expType":"ROYAL_EXP"}],"levelName":"平民","levelValue":0},{"levelType":"USER_LEVEL","expList":[{"sjExpvalue":516750,"bjExpValue":516750,"expName":"用户经验","sjNeedExpValue":800000,"bjNeedExpValue":0,"expType":"USER_EXP"}],"levelName":"富豪10","levelValue":11}],"freeGiftMaxNum":5,"avatar":"http://img.test.chengxing.tv/avatar/016/07/07/29_100x100.jpg?1435023731","city":"温州","isSubs":1,"isGuard":0,"prettyNum":0,"subscribe":1,"userId":16070729,"province":"浙江","gender":1,"wealth":{"chengPonit":100,"coin":99483249}}
     * code : 200
     * msg : success
     */
    private DataEntity data;


    public void setData(DataEntity data) {
        this.data = data;
    }


    public DataEntity getData() {
        return data;
    }


    public class DataEntity {
        private int isVip;
        private String nickname;
        private String introduce;
        private List<LevelListEntity> levelList;
        private int freeGiftMaxNum;
        private String avatar;
        private String city;
        private int isSubs;
        private int isGuard;
        private int prettyNum;
        private int subscribe;
        private int userId;
        private String province;
        private int gender;
        private WealthEntity wealth;

        public void setIsVip(int isVip) {
            this.isVip = isVip;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public void setIntroduce(String introduce) {
            this.introduce = introduce;
        }

        public void setLevelList(List<LevelListEntity> levelList) {
            this.levelList = levelList;
        }

        public void setFreeGiftMaxNum(int freeGiftMaxNum) {
            this.freeGiftMaxNum = freeGiftMaxNum;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public void setIsSubs(int isSubs) {
            this.isSubs = isSubs;
        }

        public void setIsGuard(int isGuard) {
            this.isGuard = isGuard;
        }

        public void setPrettyNum(int prettyNum) {
            this.prettyNum = prettyNum;
        }

        public void setSubscribe(int subscribe) {
            this.subscribe = subscribe;
        }

        public void setUserId(int userId) {
            this.userId = userId;
        }

        public void setProvince(String province) {
            this.province = province;
        }

        public void setGender(int gender) {
            this.gender = gender;
        }

        public void setWealth(WealthEntity wealth) {
            this.wealth = wealth;
        }

        public int getIsVip() {
            return isVip;
        }

        public String getNickname() {
            return nickname;
        }

        public String getIntroduce() {
            return introduce;
        }

        public List<LevelListEntity> getLevelList() {
            return levelList;
        }

        public int getFreeGiftMaxNum() {
            return freeGiftMaxNum;
        }

        public String getAvatar() {
            return avatar;
        }

        public String getCity() {
            return city;
        }

        public int getIsSubs() {
            return isSubs;
        }

        public int getIsGuard() {
            return isGuard;
        }

        public int getPrettyNum() {
            return prettyNum;
        }

        public int getSubscribe() {
            return subscribe;
        }

        public int getUserId() {
            return userId;
        }

        public String getProvince() {
            return province;
        }

        public int getGender() {
            return gender;
        }

        public WealthEntity getWealth() {
            return wealth;
        }

        public class LevelListEntity {
            /**
             * levelType : ROYAL_LEVEL
             * expList : [{"sjExpvalue":0,"bjExpValue":0,"expName":"贵族经验","sjNeedExpValue":200,"bjNeedExpValue":0,"expType":"ROYAL_EXP"}]
             * levelName : 平民
             * levelValue : 0
             */
            private String levelType;
            private List<ExpListEntity> expList;
            private String levelName;
            private int levelValue;

            public void setLevelType(String levelType) {
                this.levelType = levelType;
            }

            public void setExpList(List<ExpListEntity> expList) {
                this.expList = expList;
            }

            public void setLevelName(String levelName) {
                this.levelName = levelName;
            }

            public void setLevelValue(int levelValue) {
                this.levelValue = levelValue;
            }

            public String getLevelType() {
                return levelType;
            }

            public List<ExpListEntity> getExpList() {
                return expList;
            }

            public String getLevelName() {
                return levelName;
            }

            public int getLevelValue() {
                return levelValue;
            }

            public class ExpListEntity {
                /**
                 * sjExpvalue : 0
                 * bjExpValue : 0
                 * expName : 贵族经验
                 * sjNeedExpValue : 200
                 * bjNeedExpValue : 0
                 * expType : ROYAL_EXP
                 */
                private int sjExpvalue;
                private int bjExpValue;
                private String expName;
                private int sjNeedExpValue;
                private int bjNeedExpValue;
                private String expType;

                public void setSjExpvalue(int sjExpvalue) {
                    this.sjExpvalue = sjExpvalue;
                }

                public void setBjExpValue(int bjExpValue) {
                    this.bjExpValue = bjExpValue;
                }

                public void setExpName(String expName) {
                    this.expName = expName;
                }

                public void setSjNeedExpValue(int sjNeedExpValue) {
                    this.sjNeedExpValue = sjNeedExpValue;
                }

                public void setBjNeedExpValue(int bjNeedExpValue) {
                    this.bjNeedExpValue = bjNeedExpValue;
                }

                public void setExpType(String expType) {
                    this.expType = expType;
                }

                public int getSjExpvalue() {
                    return sjExpvalue;
                }

                public int getBjExpValue() {
                    return bjExpValue;
                }

                public String getExpName() {
                    return expName;
                }

                public int getSjNeedExpValue() {
                    return sjNeedExpValue;
                }

                public int getBjNeedExpValue() {
                    return bjNeedExpValue;
                }

                public String getExpType() {
                    return expType;
                }
            }
        }

        public class WealthEntity {
            /**
             * chengPonit : 100
             * coin : 99483249
             */
            private int chengPonit;
            private int coin;

            public void setChengPonit(int chengPonit) {
                this.chengPonit = chengPonit;
            }

            public void setCoin(int coin) {
                this.coin = coin;
            }

            public int getChengPonit() {
                return chengPonit;
            }

            public int getCoin() {
                return coin;
            }
        }
    }
}
