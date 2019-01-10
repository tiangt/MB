package com.whzl.mengbi.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author nobody
 * @date 2019/1/10
 */
public class GoodsPriceBatchBean implements Parcelable {

    public List<ListBean> list;

    public static class ListBean implements Parcelable {
        /**
         * rent : 100
         * priceId : 3448
         * goodsId : 94320
         * goodsEngName : 520
         * goodsName : 幸运520
         * goodsType : GIFT
         * goodsPic : http://localtest-img.mengbitv.com/default/000/00/03/47_144x144.jpg
         * userExp : 100
         * anchorExp : 40
         */

        public int rent;
        public int priceId;
        public int goodsId;
        public String goodsEngName;
        public String goodsName;
        public String goodsType;
        public String goodsPic;
        public int userExp;
        public int anchorExp;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.rent);
            dest.writeInt(this.priceId);
            dest.writeInt(this.goodsId);
            dest.writeString(this.goodsEngName);
            dest.writeString(this.goodsName);
            dest.writeString(this.goodsType);
            dest.writeString(this.goodsPic);
            dest.writeInt(this.userExp);
            dest.writeInt(this.anchorExp);
        }

        public ListBean() {
        }

        protected ListBean(Parcel in) {
            this.rent = in.readInt();
            this.priceId = in.readInt();
            this.goodsId = in.readInt();
            this.goodsEngName = in.readString();
            this.goodsName = in.readString();
            this.goodsType = in.readString();
            this.goodsPic = in.readString();
            this.userExp = in.readInt();
            this.anchorExp = in.readInt();
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

    public GoodsPriceBatchBean() {
    }

    protected GoodsPriceBatchBean(Parcel in) {
        this.list = new ArrayList<ListBean>();
        in.readList(this.list, ListBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<GoodsPriceBatchBean> CREATOR = new Parcelable.Creator<GoodsPriceBatchBean>() {
        @Override
        public GoodsPriceBatchBean createFromParcel(Parcel source) {
            return new GoodsPriceBatchBean(source);
        }

        @Override
        public GoodsPriceBatchBean[] newArray(int size) {
            return new GoodsPriceBatchBean[size];
        }
    };
}
