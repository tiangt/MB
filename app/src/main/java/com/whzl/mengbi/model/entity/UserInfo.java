package com.whzl.mengbi.model.entity;


import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * @author shaw
 */
public class UserInfo extends ResponseInfo implements Parcelable {

    private DataBean data;

    protected UserInfo(Parcel in) {
        data = in.readParcelable(DataBean.class.getClassLoader());
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel in) {
            return new UserInfo(in);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(data, flags);
    }


    public static class DataBean implements Parcelable {

        private long userId;
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

        protected DataBean(Parcel in) {
            userId = in.readLong();
            nickname = in.readString();
            avatar = in.readString();
            userType = in.readString();
            province = in.readString();
            city = in.readString();
            gender = in.readString();
            birthday = in.readString();
            introduce = in.readString();
            wealth = in.readParcelable(WealthBean.class.getClassLoader());
            sessionId = in.readString();
            levelList = in.createTypedArrayList(LevelListBean.CREATOR);
        }

        public static final Creator<DataBean> CREATOR = new Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel in) {
                return new DataBean(in);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };

        public long getUserId() {
            return userId;
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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(userId);
            dest.writeString(nickname);
            dest.writeString(avatar);
            dest.writeString(userType);
            dest.writeString(province);
            dest.writeString(city);
            dest.writeString(gender);
            dest.writeString(birthday);
            dest.writeString(introduce);
            dest.writeParcelable(wealth, flags);
            dest.writeString(sessionId);
            dest.writeTypedList(levelList);
        }

        public static class WealthBean implements Parcelable {

            private long coin;
            private long chengPonit;

            protected WealthBean(Parcel in) {
                coin = in.readLong();
                chengPonit = in.readLong();
            }

            public static final Creator<WealthBean> CREATOR = new Creator<WealthBean>() {
                @Override
                public WealthBean createFromParcel(Parcel in) {
                    return new WealthBean(in);
                }

                @Override
                public WealthBean[] newArray(int size) {
                    return new WealthBean[size];
                }
            };

            public long getCoin() {
                return coin;
            }

            public long getChengPonit() {
                return chengPonit;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeLong(coin);
                dest.writeLong(chengPonit);
            }
        }

        public static class LevelListBean implements Parcelable {

            private String levelType;
            private int levelValue;
            private String levelName;
            private List<ExpListBean> expList;

            protected LevelListBean(Parcel in) {
                levelType = in.readString();
                levelValue = in.readInt();
                levelName = in.readString();
                expList = in.createTypedArrayList(ExpListBean.CREATOR);
            }

            public static final Creator<LevelListBean> CREATOR = new Creator<LevelListBean>() {
                @Override
                public LevelListBean createFromParcel(Parcel in) {
                    return new LevelListBean(in);
                }

                @Override
                public LevelListBean[] newArray(int size) {
                    return new LevelListBean[size];
                }
            };

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

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(levelType);
                dest.writeInt(levelValue);
                dest.writeString(levelName);
                dest.writeTypedList(expList);
            }

            public static class ExpListBean implements Parcelable {

                private String expType;
                private String expName;
                private long sjExpvalue;
                private long bjExpValue;
                private long sjNeedExpValue;
                private long bjNeedExpValue;

                protected ExpListBean(Parcel in) {
                    expType = in.readString();
                    expName = in.readString();
                    sjExpvalue = in.readLong();
                    bjExpValue = in.readLong();
                    sjNeedExpValue = in.readLong();
                    bjNeedExpValue = in.readLong();
                }

                public static final Creator<ExpListBean> CREATOR = new Creator<ExpListBean>() {
                    @Override
                    public ExpListBean createFromParcel(Parcel in) {
                        return new ExpListBean(in);
                    }

                    @Override
                    public ExpListBean[] newArray(int size) {
                        return new ExpListBean[size];
                    }
                };

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

                public void setBjExpValue(long bjExpValue) {
                    this.bjExpValue = bjExpValue;
                }

                public long getSjNeedExpValue() {
                    return sjNeedExpValue;
                }

                public void setSjNeedExpValue(long sjNeedExpValue) {
                    this.sjNeedExpValue = sjNeedExpValue;
                }

                public long getBjNeedExpValue() {
                    return bjNeedExpValue;
                }

                public void setBjNeedExpValue(long bjNeedExpValue) {
                    this.bjNeedExpValue = bjNeedExpValue;
                }

                @Override
                public int describeContents() {
                    return 0;
                }

                @Override
                public void writeToParcel(Parcel dest, int flags) {
                    dest.writeString(expType);
                    dest.writeString(expName);
                    dest.writeLong(sjExpvalue);
                    dest.writeLong(bjExpValue);
                    dest.writeLong(sjNeedExpValue);
                    dest.writeLong(bjNeedExpValue);
                }
            }
        }
    }
}
