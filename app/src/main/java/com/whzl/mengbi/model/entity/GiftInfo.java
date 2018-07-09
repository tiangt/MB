package com.whzl.mengbi.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class GiftInfo extends ResponseInfo {

    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public class DataBean {
        private ArrayList<GiftDetailInfoBean> 推荐;
        private ArrayList<GiftDetailInfoBean> 豪华;
        private ArrayList<GiftDetailInfoBean> 幸运;
        private ArrayList<GiftDetailInfoBean> 普通;

        public ArrayList<GiftDetailInfoBean> get推荐() {
            return 推荐;
        }

        public void set推荐(ArrayList<GiftDetailInfoBean> 推荐) {
            this.推荐 = 推荐;
        }

        public ArrayList<GiftDetailInfoBean> get豪华() {
            return 豪华;
        }

        public void set豪华(ArrayList<GiftDetailInfoBean> 豪华) {
            this.豪华 = 豪华;
        }

        public ArrayList<GiftDetailInfoBean> get幸运() {
            return 幸运;
        }

        public void set幸运(ArrayList<GiftDetailInfoBean> 幸运) {
            this.幸运 = 幸运;
        }

        public ArrayList<GiftDetailInfoBean> get普通() {
            return 普通;
        }

        public void set普通(ArrayList<GiftDetailInfoBean> 普通) {
            this.普通 = 普通;
        }


    }

    public class GiftDetailInfoBean implements Parcelable{
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
        private boolean selected;

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
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