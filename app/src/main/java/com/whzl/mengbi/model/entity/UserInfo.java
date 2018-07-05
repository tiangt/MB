package com.whzl.mengbi.model.entity;


import java.io.Serializable;
import java.util.List;

public class UserInfo extends ResponseInfo implements Serializable{

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }


    public static class DataBean implements Serializable{

        private int userId;
        private String nickname;
        private String avatar;
        private String userType;
        private String province;
        private String city;
        private String gender;
        private String birthday;
        private String introduce;
        private WealthBean wealth;
        private String sessionId;
        private List<LevelListBean> levelList;

        public int getUserId() {
            return userId;
        }

        public void setUserId(int userId) {
            this.userId = userId;
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

        public String getUserType() {
            return userType;
        }

        public void setUserType(String userType) {
            this.userType = userType;
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

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public String getIntroduce() {
            return introduce;
        }

        public void setIntroduce(String introduce) {
            this.introduce = introduce;
        }

        public WealthBean getWealth() {
            return wealth;
        }

        public void setWealth(WealthBean wealth) {
            this.wealth = wealth;
        }

        public String getSessionId() {
            return sessionId;
        }

        public void setSessionId(String sessionId) {
            this.sessionId = sessionId;
        }

        public List<LevelListBean> getLevelList() {
            return levelList;
        }

        public void setLevelList(List<LevelListBean> levelList) {
            this.levelList = levelList;
        }

        public static class WealthBean implements Serializable{

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

        public static class LevelListBean implements Serializable{

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

            public static class ExpListBean implements Serializable{

                private String expType;
                private String expName;
                private int sjExpvalue;
                private int bjExpValue;
                private int sjNeedExpValue;
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

                public int getSjExpvalue() {
                    return sjExpvalue;
                }

                public void setSjExpvalue(int sjExpvalue) {
                    this.sjExpvalue = sjExpvalue;
                }

                public int getBjExpValue() {
                    return bjExpValue;
                }

                public void setBjExpValue(int bjExpValue) {
                    this.bjExpValue = bjExpValue;
                }

                public int getSjNeedExpValue() {
                    return sjNeedExpValue;
                }

                public void setSjNeedExpValue(int sjNeedExpValue) {
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
    }
}
