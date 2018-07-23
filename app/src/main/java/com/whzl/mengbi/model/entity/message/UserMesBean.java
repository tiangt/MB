package com.whzl.mengbi.model.entity.message;

import java.util.List;

public class UserMesBean {

    private ContextBean context;
    private String display;
    private String eventCode;
    private String msgType;
    private String platform;
    private String type;

    public ContextBean getContext() {
        return context;
    }

    public void setContext(ContextBean context) {
        this.context = context;
    }

    public String getDisplay() {
        return display;
    }

    public void setDisplay(String display) {
        this.display = display;
    }

    public String getEventCode() {
        return eventCode;
    }

    public void setEventCode(String eventCode) {
        this.eventCode = eventCode;
    }

    public String getMsgType() {
        return msgType;
    }

    public void setMsgType(String msgType) {
        this.msgType = msgType;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public static class ContextBean {

        private InfoBean info;

        public InfoBean getInfo() {
            return info;
        }

        public void setInfo(InfoBean info) {
            this.info = info;
        }

        public static class InfoBean {
            private AnchorScoreDTOBean anchorScoreDTO;
            private long createTime;
            private String ip;
            private long lastLoginTime;
            private String lastPlatformType;
            private long lastUpdateTime;
            private String loginName;
            private String mobile;
            private String mobileStatus;
            private String nickname;
            private String registerType;
            private String status;
            private long userId;
            private UserIdentityMapBean userIdentityMap;
            private UserSetMapBean userSetMap;
            private UserTagMapBean userTagMap;
            private String userType;
            private WealthMapBean wealthMap;
            private List<LevelListBean> levelList;
            private List<UserBagListBean> userBagList;
            private List<Integer> userStoreProgramList;
            private List<?> userSubsProgramList;

            public AnchorScoreDTOBean getAnchorScoreDTO() {
                return anchorScoreDTO;
            }

            public void setAnchorScoreDTO(AnchorScoreDTOBean anchorScoreDTO) {
                this.anchorScoreDTO = anchorScoreDTO;
            }

            public long getCreateTime() {
                return createTime;
            }

            public void setCreateTime(long createTime) {
                this.createTime = createTime;
            }

            public String getIp() {
                return ip;
            }

            public void setIp(String ip) {
                this.ip = ip;
            }

            public long getLastLoginTime() {
                return lastLoginTime;
            }

            public void setLastLoginTime(long lastLoginTime) {
                this.lastLoginTime = lastLoginTime;
            }

            public String getLastPlatformType() {
                return lastPlatformType;
            }

            public void setLastPlatformType(String lastPlatformType) {
                this.lastPlatformType = lastPlatformType;
            }

            public long getLastUpdateTime() {
                return lastUpdateTime;
            }

            public void setLastUpdateTime(long lastUpdateTime) {
                this.lastUpdateTime = lastUpdateTime;
            }

            public String getLoginName() {
                return loginName;
            }

            public void setLoginName(String loginName) {
                this.loginName = loginName;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getMobileStatus() {
                return mobileStatus;
            }

            public void setMobileStatus(String mobileStatus) {
                this.mobileStatus = mobileStatus;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getRegisterType() {
                return registerType;
            }

            public void setRegisterType(String registerType) {
                this.registerType = registerType;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public long getUserId() {
                return userId;
            }

            public UserIdentityMapBean getUserIdentityMap() {
                return userIdentityMap;
            }

            public void setUserIdentityMap(UserIdentityMapBean userIdentityMap) {
                this.userIdentityMap = userIdentityMap;
            }

            public UserSetMapBean getUserSetMap() {
                return userSetMap;
            }

            public void setUserSetMap(UserSetMapBean userSetMap) {
                this.userSetMap = userSetMap;
            }

            public UserTagMapBean getUserTagMap() {
                return userTagMap;
            }

            public void setUserTagMap(UserTagMapBean userTagMap) {
                this.userTagMap = userTagMap;
            }

            public String getUserType() {
                return userType;
            }

            public void setUserType(String userType) {
                this.userType = userType;
            }

            public WealthMapBean getWealthMap() {
                return wealthMap;
            }

            public void setWealthMap(WealthMapBean wealthMap) {
                this.wealthMap = wealthMap;
            }

            public List<LevelListBean> getLevelList() {
                return levelList;
            }

            public void setLevelList(List<LevelListBean> levelList) {
                this.levelList = levelList;
            }

            public List<UserBagListBean> getUserBagList() {
                return userBagList;
            }

            public void setUserBagList(List<UserBagListBean> userBagList) {
                this.userBagList = userBagList;
            }

            public List<Integer> getUserStoreProgramList() {
                return userStoreProgramList;
            }

            public void setUserStoreProgramList(List<Integer> userStoreProgramList) {
                this.userStoreProgramList = userStoreProgramList;
            }

            public List<?> getUserSubsProgramList() {
                return userSubsProgramList;
            }

            public void setUserSubsProgramList(List<?> userSubsProgramList) {
                this.userSubsProgramList = userSubsProgramList;
            }

            public static class AnchorScoreDTOBean {
            }

            public static class UserIdentityMapBean {
            }

            public static class UserSetMapBean {
            }

            public static class UserTagMapBean {
            }

            public static class WealthMapBean {
                /**
                 * COIN : 9999900
                 */

                private int COIN;

                public int getCOIN() {
                    return COIN;
                }

                public void setCOIN(int COIN) {
                    this.COIN = COIN;
                }
            }

            public static class LevelListBean {

                private long beginTime;
                private String levelName;
                private String levelPic;
                private String levelType;
                private int levelValue;
                private String remark;
                private int userLevelSn;
                private List<ExpListBean> expList;

                public long getBeginTime() {
                    return beginTime;
                }

                public void setBeginTime(long beginTime) {
                    this.beginTime = beginTime;
                }

                public String getLevelName() {
                    return levelName;
                }

                public void setLevelName(String levelName) {
                    this.levelName = levelName;
                }

                public String getLevelPic() {
                    return levelPic;
                }

                public void setLevelPic(String levelPic) {
                    this.levelPic = levelPic;
                }

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

                public String getRemark() {
                    return remark;
                }

                public void setRemark(String remark) {
                    this.remark = remark;
                }

                public int getUserLevelSn() {
                    return userLevelSn;
                }

                public void setUserLevelSn(int userLevelSn) {
                    this.userLevelSn = userLevelSn;
                }

                public List<ExpListBean> getExpList() {
                    return expList;
                }

                public void setExpList(List<ExpListBean> expList) {
                    this.expList = expList;
                }

                public static class ExpListBean {

                    private int bjExpValue;
                    private int bjNeedExpValue;
                    private String expName;
                    private String expType;
                    private int sjExpvalue;
                    private int sjNeedExpValue;
                    private int totalExpValue;
                    private int userExpSn;

                    public int getBjExpValue() {
                        return bjExpValue;
                    }

                    public void setBjExpValue(int bjExpValue) {
                        this.bjExpValue = bjExpValue;
                    }

                    public int getBjNeedExpValue() {
                        return bjNeedExpValue;
                    }

                    public void setBjNeedExpValue(int bjNeedExpValue) {
                        this.bjNeedExpValue = bjNeedExpValue;
                    }

                    public String getExpName() {
                        return expName;
                    }

                    public void setExpName(String expName) {
                        this.expName = expName;
                    }

                    public String getExpType() {
                        return expType;
                    }

                    public void setExpType(String expType) {
                        this.expType = expType;
                    }

                    public int getSjExpvalue() {
                        return sjExpvalue;
                    }

                    public void setSjExpvalue(int sjExpvalue) {
                        this.sjExpvalue = sjExpvalue;
                    }

                    public int getSjNeedExpValue() {
                        return sjNeedExpValue;
                    }

                    public void setSjNeedExpValue(int sjNeedExpValue) {
                        this.sjNeedExpValue = sjNeedExpValue;
                    }

                    public int getTotalExpValue() {
                        return totalExpValue;
                    }

                    public void setTotalExpValue(int totalExpValue) {
                        this.totalExpValue = totalExpValue;
                    }

                    public int getUserExpSn() {
                        return userExpSn;
                    }

                    public void setUserExpSn(int userExpSn) {
                        this.userExpSn = userExpSn;
                    }
                }
            }

            public static class UserBagListBean {

                private boolean colorSpeak;
                private int equipDay;
                private long feeTime;
                private int goodsId;
                private String goodsName;
                private int goodsPicId;
                private int goodsSn;
                private String goodsType;
                private String isEquip;
                private int surplusDay;
                private long userId;

                public boolean isColorSpeak() {
                    return colorSpeak;
                }

                public void setColorSpeak(boolean colorSpeak) {
                    this.colorSpeak = colorSpeak;
                }

                public int getEquipDay() {
                    return equipDay;
                }

                public void setEquipDay(int equipDay) {
                    this.equipDay = equipDay;
                }

                public long getFeeTime() {
                    return feeTime;
                }

                public void setFeeTime(long feeTime) {
                    this.feeTime = feeTime;
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

                public int getGoodsPicId() {
                    return goodsPicId;
                }

                public void setGoodsPicId(int goodsPicId) {
                    this.goodsPicId = goodsPicId;
                }

                public int getGoodsSn() {
                    return goodsSn;
                }

                public void setGoodsSn(int goodsSn) {
                    this.goodsSn = goodsSn;
                }

                public String getGoodsType() {
                    return goodsType;
                }

                public void setGoodsType(String goodsType) {
                    this.goodsType = goodsType;
                }

                public String getIsEquip() {
                    return isEquip;
                }

                public void setIsEquip(String isEquip) {
                    this.isEquip = isEquip;
                }

                public int getSurplusDay() {
                    return surplusDay;
                }

                public void setSurplusDay(int surplusDay) {
                    this.surplusDay = surplusDay;
                }

                public long getUserId() {
                    return userId;
                }

            }
        }
    }
}
