package com.whzl.mengbi.chat.room.message.messageJson;

import java.util.List;

public class WelcomeJson {
    public class WelcomeExpListItem {
        private int sjExpvalue;
        private int bjExpValue;
        private int userExpSn;
        private String expName;
        private int sjNeedExpValue;
        private int bjNeedExpValue;
        private int totalExpValue;
        private String expType;

        public int getSjExpvalue() {
            return sjExpvalue;
        }

        public int getBjExpValue() {
            return bjExpValue;
        }

        public int getUserExpSn() {
            return userExpSn;
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

        public int getTotalExpValue() {
            return totalExpValue;
        }

        public String getExpType() {
            return expType;
        }
    }

    public class WelcomeLevelListItem {
        List<WelcomeExpListItem> levelList;
        String levelName;
        String levelPic;
        String levelType;
        int levelValue;
        String remark;

        public List<WelcomeExpListItem> getLevelList() {
            return levelList;
        }

        public String getLevelName() {
            return levelName;
        }

        public String getLevelPic() {
            return levelPic;
        }

        public String getLevelType() {
            return levelType;
        }

        public int getLevelValue() {
            return levelValue;
        }

        public String getRemark() {
            return remark;
        }
    }

    public class UserBagItem {
        int equipDay;
        String goodsType;

        public String getPrettyNum() {
            return prettyNum;
        }

        String prettyNum;
        String isEquip;
        int goodsPicId;
        String goodsName;
        int bindProgramId = 0;

        public int getBindProgramId() {
            return bindProgramId;
        }

        public void setBindProgramId(int bindProgramId) {
            this.bindProgramId = bindProgramId;
        }

        public String getGoodsName() {
            return goodsName;
        }

        public int getEquipDay() {
            return equipDay;
        }

        public String getGoodsType() {
            return goodsType;
        }

        public String getIsEquip() {
            return isEquip;
        }

        public int getGoodsPicId() {
            return goodsPicId;
        }
    }

    public class WelcomeInfo {
        String nickname;
        String certNum;
        long userId;
        List<WelcomeLevelListItem> levelList;
        List<UserBagItem> userBagList;

        public long getUserId() {
            return userId;
        }

        public List<UserBagItem> getUserBagList() {
            return userBagList;
        }

        public String getNickname() {
            return nickname;
        }

        public String getCertNum() {
            return certNum;
        }

        public List<WelcomeLevelListItem> getLevelList() {
            return levelList;
        }
    }

    public class WelcomeContext {
        WelcomeInfo info;
        CarObj carObj;
        public CarObj getCarObj() {
            return carObj;
        }
        public WelcomeInfo getInfo() {
            return info;
        }
    }

    WelcomeContext context;
    String eventCode;

    public WelcomeContext getContext() {
        return context;
    }

    public String getEventCode() {
        return eventCode;
    }

    public class CarObj{
        public int getCarPicId() {
            return carPicId;
        }

        public long getPrettyNumberOrUserId() {
            if(prettyNumberOrUserId == null){
                return 0;
            }
            return Long.parseLong(prettyNumberOrUserId);
        }

        public String getGoodsColor() {
            return goodsColor;
        }

        public int getCarGoodsId() {
            return carGoodsId;
        }

        public String getGoodsName() {
            return goodsName;
        }

        private int carPicId;
        private String prettyNumberOrUserId;
        private String goodsColor;
        private int carGoodsId;
        private String goodsName;
    }
}
