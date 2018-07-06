package com.whzl.mengbi.model.entity;

import java.util.List;

public class GiftInfo extends ResponseInfo {

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private List<推荐Bean> 推荐;
        private List<豪华Bean> 豪华;
        private List<幸运Bean> 幸运;
        private List<普通Bean> 普通;

        public List<推荐Bean> get推荐() {
            return 推荐;
        }

        public void set推荐(List<推荐Bean> 推荐) {
            this.推荐 = 推荐;
        }

        public List<豪华Bean> get豪华() {
            return 豪华;
        }

        public void set豪华(List<豪华Bean> 豪华) {
            this.豪华 = 豪华;
        }

        public List<幸运Bean> get幸运() {
            return 幸运;
        }

        public void set幸运(List<幸运Bean> 幸运) {
            this.幸运 = 幸运;
        }

        public List<普通Bean> get普通() {
            return 普通;
        }

        public void set普通(List<普通Bean> 普通) {
            this.普通 = 普通;
        }

        public static class 推荐Bean {
            private int goodsId;
            private String goodsName;
            private Object goodsEngName;
            private Object sortNum;
            private int picId;
            private String goodsTypeName;
            private int rent;
            private String feeType;
            private Object tagId;
            private Object tagName;
            private String goodPic;

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

            public Object getGoodsEngName() {
                return goodsEngName;
            }

            public void setGoodsEngName(Object goodsEngName) {
                this.goodsEngName = goodsEngName;
            }

            public Object getSortNum() {
                return sortNum;
            }

            public void setSortNum(Object sortNum) {
                this.sortNum = sortNum;
            }

            public int getPicId() {
                return picId;
            }

            public void setPicId(int picId) {
                this.picId = picId;
            }

            public String getGoodsTypeName() {
                return goodsTypeName;
            }

            public void setGoodsTypeName(String goodsTypeName) {
                this.goodsTypeName = goodsTypeName;
            }

            public int getRent() {
                return rent;
            }

            public void setRent(int rent) {
                this.rent = rent;
            }

            public String getFeeType() {
                return feeType;
            }

            public void setFeeType(String feeType) {
                this.feeType = feeType;
            }

            public Object getTagId() {
                return tagId;
            }

            public void setTagId(Object tagId) {
                this.tagId = tagId;
            }

            public Object getTagName() {
                return tagName;
            }

            public void setTagName(Object tagName) {
                this.tagName = tagName;
            }

            public String getGoodPic() {
                return goodPic;
            }

            public void setGoodPic(String goodPic) {
                this.goodPic = goodPic;
            }
        }

        public static class 豪华Bean {

            private int goodsId;
            private String goodsName;
            private Object goodsEngName;
            private Object sortNum;
            private int picId;
            private String goodsTypeName;
            private int rent;
            private String feeType;
            private Object tagId;
            private Object tagName;
            private String goodPic;

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

            public Object getGoodsEngName() {
                return goodsEngName;
            }

            public void setGoodsEngName(Object goodsEngName) {
                this.goodsEngName = goodsEngName;
            }

            public Object getSortNum() {
                return sortNum;
            }

            public void setSortNum(Object sortNum) {
                this.sortNum = sortNum;
            }

            public int getPicId() {
                return picId;
            }

            public void setPicId(int picId) {
                this.picId = picId;
            }

            public String getGoodsTypeName() {
                return goodsTypeName;
            }

            public void setGoodsTypeName(String goodsTypeName) {
                this.goodsTypeName = goodsTypeName;
            }

            public int getRent() {
                return rent;
            }

            public void setRent(int rent) {
                this.rent = rent;
            }

            public String getFeeType() {
                return feeType;
            }

            public void setFeeType(String feeType) {
                this.feeType = feeType;
            }

            public Object getTagId() {
                return tagId;
            }

            public void setTagId(Object tagId) {
                this.tagId = tagId;
            }

            public Object getTagName() {
                return tagName;
            }

            public void setTagName(Object tagName) {
                this.tagName = tagName;
            }

            public String getGoodPic() {
                return goodPic;
            }

            public void setGoodPic(String goodPic) {
                this.goodPic = goodPic;
            }
        }

        public static class 幸运Bean {

            private int goodsId;
            private String goodsName;
            private Object goodsEngName;
            private Object sortNum;
            private int picId;
            private String goodsTypeName;
            private int rent;
            private String feeType;
            private Object tagId;
            private Object tagName;
            private String goodPic;

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

            public Object getGoodsEngName() {
                return goodsEngName;
            }

            public void setGoodsEngName(Object goodsEngName) {
                this.goodsEngName = goodsEngName;
            }

            public Object getSortNum() {
                return sortNum;
            }

            public void setSortNum(Object sortNum) {
                this.sortNum = sortNum;
            }

            public int getPicId() {
                return picId;
            }

            public void setPicId(int picId) {
                this.picId = picId;
            }

            public String getGoodsTypeName() {
                return goodsTypeName;
            }

            public void setGoodsTypeName(String goodsTypeName) {
                this.goodsTypeName = goodsTypeName;
            }

            public int getRent() {
                return rent;
            }

            public void setRent(int rent) {
                this.rent = rent;
            }

            public String getFeeType() {
                return feeType;
            }

            public void setFeeType(String feeType) {
                this.feeType = feeType;
            }

            public Object getTagId() {
                return tagId;
            }

            public void setTagId(Object tagId) {
                this.tagId = tagId;
            }

            public Object getTagName() {
                return tagName;
            }

            public void setTagName(Object tagName) {
                this.tagName = tagName;
            }

            public String getGoodPic() {
                return goodPic;
            }

            public void setGoodPic(String goodPic) {
                this.goodPic = goodPic;
            }
        }

        public static class 普通Bean {

            private int goodsId;
            private String goodsName;
            private Object goodsEngName;
            private Object sortNum;
            private int picId;
            private String goodsTypeName;
            private int rent;
            private String feeType;
            private Object tagId;
            private Object tagName;
            private String goodPic;

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

            public Object getGoodsEngName() {
                return goodsEngName;
            }

            public void setGoodsEngName(Object goodsEngName) {
                this.goodsEngName = goodsEngName;
            }

            public Object getSortNum() {
                return sortNum;
            }

            public void setSortNum(Object sortNum) {
                this.sortNum = sortNum;
            }

            public int getPicId() {
                return picId;
            }

            public void setPicId(int picId) {
                this.picId = picId;
            }

            public String getGoodsTypeName() {
                return goodsTypeName;
            }

            public void setGoodsTypeName(String goodsTypeName) {
                this.goodsTypeName = goodsTypeName;
            }

            public int getRent() {
                return rent;
            }

            public void setRent(int rent) {
                this.rent = rent;
            }

            public String getFeeType() {
                return feeType;
            }

            public void setFeeType(String feeType) {
                this.feeType = feeType;
            }

            public Object getTagId() {
                return tagId;
            }

            public void setTagId(Object tagId) {
                this.tagId = tagId;
            }

            public Object getTagName() {
                return tagName;
            }

            public void setTagName(Object tagName) {
                this.tagName = tagName;
            }

            public String getGoodPic() {
                return goodPic;
            }

            public void setGoodPic(String goodPic) {
                this.goodPic = goodPic;
            }
        }
    }
}
