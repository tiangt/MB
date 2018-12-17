package com.whzl.mengbi.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author nobody
 * @date 2018/11/28
 */
public class WeekRankBean implements Parcelable {


    public List<ListBean> list;

    public static class ListBean implements Parcelable {
        /**
         * rankId : 37060
         * goodsName : 幸运520
         * goodsPic : https://test-img.mengbitv.com/api-img/week-star/37060.jpg
         * rankValue : 4
         */

        public int rankId;
        public String goodsName;
        public String goodsPic;
        public int rankValue;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.rankId);
            dest.writeString(this.goodsName);
            dest.writeString(this.goodsPic);
            dest.writeInt(this.rankValue);
        }

        public ListBean() {
        }

        protected ListBean(Parcel in) {
            this.rankId = in.readInt();
            this.goodsName = in.readString();
            this.goodsPic = in.readString();
            this.rankValue = in.readInt();
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

    public WeekRankBean() {
    }

    protected WeekRankBean(Parcel in) {
        this.list = new ArrayList<ListBean>();
        in.readList(this.list, ListBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<WeekRankBean> CREATOR = new Parcelable.Creator<WeekRankBean>() {
        @Override
        public WeekRankBean createFromParcel(Parcel source) {
            return new WeekRankBean(source);
        }

        @Override
        public WeekRankBean[] newArray(int size) {
            return new WeekRankBean[size];
        }
    };
}
