package com.whzl.mengbi.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @author nobody
 * @date 2018/9/19
 */
public class GoodNumBean {


    public List<DigitsBean> fiveDigits;
    public List<DigitsBean> sixDigits;
    public List<DigitsBean> seveDigits;

    public static class DigitsBean implements Parcelable {
        /**
         * goodsId : 94433
         * goodsName : 789456
         * priceId : 3512
         * rent : 300120
         */

        public int goodsId;
        public String goodsName;
        public int priceId;
        public int rent;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.goodsId);
            dest.writeString(this.goodsName);
            dest.writeInt(this.priceId);
            dest.writeInt(this.rent);
        }

        public DigitsBean() {
        }

        protected DigitsBean(Parcel in) {
            this.goodsId = in.readInt();
            this.goodsName = in.readString();
            this.priceId = in.readInt();
            this.rent = in.readInt();
        }

        public static final Parcelable.Creator<DigitsBean> CREATOR = new Parcelable.Creator<DigitsBean>() {
            @Override
            public DigitsBean createFromParcel(Parcel source) {
                return new DigitsBean(source);
            }

            @Override
            public DigitsBean[] newArray(int size) {
                return new DigitsBean[size];
            }
        };
    }
}
