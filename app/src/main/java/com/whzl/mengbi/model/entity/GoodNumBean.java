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
    public List<DigitsBean> fourDigits;

    public static class DigitsBean implements Parcelable {
        /**
         * goodsId : 94433
         * goodsName : 789456
         * priceId : 3512
         * rent : 300120
         */

        public int goodsId;
        public String goodsName;
        public String goodsColor;
        public int priceId;
        public long rent;

        public DigitsBean() {
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.goodsId);
            dest.writeString(this.goodsName);
            dest.writeString(this.goodsColor);
            dest.writeInt(this.priceId);
            dest.writeLong(this.rent);
        }

        protected DigitsBean(Parcel in) {
            this.goodsId = in.readInt();
            this.goodsName = in.readString();
            this.goodsColor = in.readString();
            this.priceId = in.readInt();
            this.rent = in.readLong();
        }

        public static final Creator<DigitsBean> CREATOR = new Creator<DigitsBean>() {
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
