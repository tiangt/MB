package com.whzl.mengbi.chat.room.message.messageJson;

import java.util.List;

public class WelcomeJson {
    public class WelcomeExpListItem {
        private long sjExpvalue;
        private long bjExpValue;
        private int userExpSn;
        private String expName;
        private long sjNeedExpValue;
        private long bjNeedExpValue;
        private long totalExpValue;
        private String expType;

        public long getSjExpvalue() {
            return sjExpvalue;
        }

        public long getBjExpValue() {
            return bjExpValue;
        }

        public int getUserExpSn() {
            return userExpSn;
        }

        public String getExpName() {
            return expName;
        }

        public long getSjNeedExpValue() {
            return sjNeedExpValue;
        }

        public long getBjNeedExpValue() {
            return bjNeedExpValue;
        }

        public long getTotalExpValue() {
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
        public String goodsColor;

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
        public String prettyNumberOrUserId;
        private String goodsColor;
        private int carGoodsId;
        private String goodsName;
    }
}
