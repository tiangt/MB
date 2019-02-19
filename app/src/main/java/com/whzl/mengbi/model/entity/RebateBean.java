package com.whzl.mengbi.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author nobody
 * @date 2018/10/29
 */
public class RebateBean implements Parcelable {

    public List<ListBean> list;

    public static class ListBean implements Parcelable {
        /**
         * couponId : 9
         * scale : 3
         * goodsSn : 2148
         * goodsName : 3%返利券
         * identifyCode : DGUW1O
         * goodsSum : 1
         */

        public long couponId;
        public int scale;
        public long goodsSn;
        public String goodsName;
        public String identifyCode;
        public int goodsSum;
        public String expDate;


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeLong(this.couponId);
            dest.writeInt(this.scale);
            dest.writeLong(this.goodsSn);
            dest.writeString(this.goodsName);
            dest.writeString(this.identifyCode);
            dest.writeInt(this.goodsSum);
            dest.writeString(this.expDate);
        }

        public ListBean() {
        }

        protected ListBean(Parcel in) {
            this.couponId = in.readLong();
            this.scale = in.readInt();
            this.goodsSn = in.readLong();
            this.goodsName = in.readString();
            this.identifyCode = in.readString();
            this.goodsSum = in.readInt();
            this.expDate = in.readString();
        }

        public static final Creator<ListBean> CREATOR = new Creator<ListBean>() {
            @Override
            public ListBean createFromParcel(Parcel source) {
                return new ListBean(source);
            }

            @Override
            public ListBean[] newArray(int size) {
                return new ListBean[size];
            }
        };
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.list);
    }

    public RebateBean() {
    }

    protected RebateBean(Parcel in) {
        this.list = new ArrayList<ListBean>();
        in.readList(this.list, ListBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<RebateBean> CREATOR = new Parcelable.Creator<RebateBean>() {
        @Override
        public RebateBean createFromParcel(Parcel source) {
            return new RebateBean(source);
        }

        @Override
        public RebateBean[] newArray(int size) {
            return new RebateBean[size];
        }
    };
}
