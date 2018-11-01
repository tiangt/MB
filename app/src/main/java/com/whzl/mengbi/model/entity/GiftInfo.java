package com.whzl.mengbi.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class GiftInfo extends ResponseInfo implements Parcelable {

    private DataBean data;

    protected GiftInfo(Parcel in) {
    }

    public static final Creator<GiftInfo> CREATOR = new Creator<GiftInfo>() {
        @Override
        public GiftInfo createFromParcel(Parcel in) {
            return new GiftInfo(in);
        }

        @Override
        public GiftInfo[] newArray(int size) {
            return new GiftInfo[size];
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
    }

//    public class DataBean {
//        private ArrayList<GiftDetailInfoBean> 推荐;
//        private ArrayList<GiftDetailInfoBean> 豪华;
//        private ArrayList<GiftDetailInfoBean> 幸运;
//        private ArrayList<GiftDetailInfoBean> 普通;
//
//        public ArrayList<GiftDetailInfoBean> getRecommend() {
//            return 推荐;
//        }
//
//        public void setRecommend(ArrayList<GiftDetailInfoBean> recommend) {
//            this.推荐 = 推荐;
//        }
//
//        public ArrayList<GiftDetailInfoBean> getLuxury() {
//            return 豪华;
//        }
//
//        public void setLuxury(ArrayList<GiftDetailInfoBean> 豪华) {
//            this.豪华 = 豪华;
//        }
//
//        public ArrayList<GiftDetailInfoBean> getLucky() {
//            return 幸运;
//        }
//
//        public void setLucky(ArrayList<GiftDetailInfoBean> 幸运) {
//            this.幸运 = 幸运;
//        }
//
//        public ArrayList<GiftDetailInfoBean> getCommon() {
//            return 普通;
//        }
//
//        public void setCommon(ArrayList<GiftDetailInfoBean> 普通) {
//            this.普通 = 普通;
//        }
//
//
//    }

    public class  DataBean{
        private List<DataBean.ListBean> list;

        public List<DataBean.ListBean> getList() {
            return list;
        }

        public void setList(List<DataBean.ListBean> list) {
            this.list = list;
        }

        public class ListBean {

            private String group;
            private ArrayList<GiftDetailInfoBean> groupList;

            public String getGroup() {
                return group;
            }

            public void setGroup(String group) {
                this.group = group;
            }

            public ArrayList<GiftDetailInfoBean> getGroupList() {
                return groupList;
            }

            public void setGroupList(ArrayList<GiftDetailInfoBean> groupList) {
                this.groupList = groupList;
            }
        }
    }

    public static class GiftDetailInfoBean implements Parcelable {
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
        private boolean isBackpack;

        public boolean isBackpack() {
            return isBackpack;
        }

        public void setBackpack(boolean backpack) {
            this.isBackpack = backpack;
        }


        protected GiftDetailInfoBean(Parcel in) {
            goodsId = in.readInt();
            goodsName = in.readString();
            picId = in.readInt();
            goodsTypeName = in.readString();
            rent = in.readInt();
            feeType = in.readString();
            goodPic = in.readString();
        }

        public GiftDetailInfoBean(){}

        public final Creator<GiftDetailInfoBean> CREATOR = new Creator<GiftDetailInfoBean>() {
            @Override
            public GiftDetailInfoBean createFromParcel(Parcel in) {
                return new GiftDetailInfoBean(in);
            }

            @Override
            public GiftDetailInfoBean[] newArray(int size) {
                return new GiftDetailInfoBean[size];
            }
        };

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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(goodsId);
            dest.writeString(goodsName);
            dest.writeInt(picId);
            dest.writeString(goodsTypeName);
            dest.writeInt(rent);
            dest.writeString(feeType);
            dest.writeString(goodPic);
        }
    }


}