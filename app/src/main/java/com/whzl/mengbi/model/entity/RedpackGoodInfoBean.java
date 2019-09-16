package com.whzl.mengbi.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * @author nobody
 * @date 2019-09-10
 */
public class RedpackGoodInfoBean implements Parcelable {


    public ArrayList<ConditionGoodListBean> conditionGoodList;
    public ArrayList<PrizeGoodsListBean> prizeGoodsList;
    public long minCoinNum;

    public static class ConditionGoodListBean implements Parcelable {
        /**
         * conditionGoodsCfgId : 1
         * goodsId : 94329
         * goodsType : GOODS
         * goodsName : 静一静
         */

        public int conditionGoodsCfgId;
        public int goodsId;
        public String goodsType;
        public String goodsName;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.conditionGoodsCfgId);
            dest.writeInt(this.goodsId);
            dest.writeString(this.goodsType);
            dest.writeString(this.goodsName);
        }

        public ConditionGoodListBean() {
        }

        protected ConditionGoodListBean(Parcel in) {
            this.conditionGoodsCfgId = in.readInt();
            this.goodsId = in.readInt();
            this.goodsType = in.readString();
            this.goodsName = in.readString();
        }

        public static final Parcelable.Creator<ConditionGoodListBean> CREATOR = new Parcelable.Creator<ConditionGoodListBean>() {
            @Override
            public ConditionGoodListBean createFromParcel(Parcel source) {
                return new ConditionGoodListBean(source);
            }

            @Override
            public ConditionGoodListBean[] newArray(int size) {
                return new ConditionGoodListBean[size];
            }
        };
    }

    public static class PrizeGoodsListBean implements Parcelable {
        /**
         * goodsPrice : 100
         * minNum : 1000
         * multipleNum : 100
         * prizeGoodsCfgId : 2
         */

        public long goodsPrice;
        public int minNum;
        public int multipleNum;
        public int prizeGoodsCfgId;
        public String goodsName;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(this.goodsPrice);
            dest.writeInt(this.minNum);
            dest.writeInt(this.multipleNum);
            dest.writeInt(this.prizeGoodsCfgId);
            dest.writeString(this.goodsName);
        }

        public PrizeGoodsListBean() {
        }

        protected PrizeGoodsListBean(Parcel in) {
            this.goodsPrice = in.readInt();
            this.minNum = in.readInt();
            this.multipleNum = in.readInt();
            this.prizeGoodsCfgId = in.readInt();
            this.goodsName = in.readString();
        }

        public static final Parcelable.Creator<PrizeGoodsListBean> CREATOR = new Parcelable.Creator<PrizeGoodsListBean>() {
            @Override
            public PrizeGoodsListBean createFromParcel(Parcel source) {
                return new PrizeGoodsListBean(source);
            }

            @Override
            public PrizeGoodsListBean[] newArray(int size) {
                return new PrizeGoodsListBean[size];
            }
        };
    }

    public RedpackGoodInfoBean() {
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.conditionGoodList);
        dest.writeTypedList(this.prizeGoodsList);
        dest.writeLong(this.minCoinNum);
    }

    protected RedpackGoodInfoBean(Parcel in) {
        this.conditionGoodList = in.createTypedArrayList(ConditionGoodListBean.CREATOR);
        this.prizeGoodsList = in.createTypedArrayList(PrizeGoodsListBean.CREATOR);
        this.minCoinNum = in.readLong();
    }

    public static final Creator<RedpackGoodInfoBean> CREATOR = new Creator<RedpackGoodInfoBean>() {
        @Override
        public RedpackGoodInfoBean createFromParcel(Parcel source) {
            return new RedpackGoodInfoBean(source);
        }

        @Override
        public RedpackGoodInfoBean[] newArray(int size) {
            return new RedpackGoodInfoBean[size];
        }
    };
}
