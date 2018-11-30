package com.whzl.mengbi.chat.room.message.messageJson;

import java.util.List;


public class FromJson {
    List<Good> goodsList;
    List<Level> levelList;

    public List<Good> getGoodsList() {
        return goodsList;
    }

    public List<Level> getLevelList() {
        return levelList;
    }


    public class Level{
        List<Exp> expList;
        String levelName;
        String levelPic;
        String levelType;
        int levelValue;
        String remark;
        long userLevelSn;

        public List<Exp> getExpList() {
            return expList;
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

        public long getUserLevelSn() {
            return userLevelSn;
        }
    }

    public class Good{
        int goodsIcon;
        int goodsId;
        String goodsName;
        String goodsType;
        int bindProgramId;
        public String goodsColor;

        public int getGoodsIcon() {
            return goodsIcon;
        }

        public void setGoodsIcon(int goodsIcon) {
            this.goodsIcon = goodsIcon;
        }

        public String getGoodsName() {
            return goodsName;
        }

        public void setGoodsName(String goodsName) {
            this.goodsName = goodsName;
        }

        public int getGoodsId() {
            return goodsId;
        }

        public void setGoodsId(int goodsId) {
            this.goodsId = goodsId;
        }

        public String getGoodsType() {
            return goodsType;
        }

        public void setGoodsType(String goodsType) {
            this.goodsType = goodsType;
        }

        public int getBindProgramId() {
            return bindProgramId;
        }

        public void setBindProgramId(int bindProgramId) {
            this.bindProgramId = bindProgramId;
        }
    }
}

class Exp{
    long bjExpValue;
    long bjNeedExpValue;
    String expName;
    String expType;
    long sjExpvalue;
    long sjNeedExpValue;
    long totalExpValue;
    long userExpSn;
}




