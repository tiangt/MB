package com.whzl.mengbi.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author nobody
 * @date 2019/3/26
 */
public class AnchorWishBean {
    public int code;
    public DataBean data;
    public String msg;
    public boolean success;

    /**
     * totalWishCard : 10000
     * giftPicUrl : xxxxx
     * awardInfo : {"officalAwardPeopleNumber":-1,"awardType":"RANDOM","awardPeopleNumber":10,"awardWealth":20000,"officalAwardType":"ORDINARY","officalAwardWealth":50000}
     * supportPeopleNum : 10
     * finishedWishCard : 100
     * giftPrice : 10000
     * giftName : huoshan
     * remainTime : 600
     */
    public static class DataBean implements Parcelable {
        public int totalWishCard;
        public String giftPicUrl;
        public AwardInfoBean awardInfo;
        public int supportPeopleNum;
        public int finishedWishCard;
        public int giftPrice;
        public long totalGiftWorth;
        public int sendGiftPrice;
        public int wishGiftNum;
        public String giftName;
        public String sendGiftName;
        public String distributeRule;
        public int remainTime;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.totalWishCard);
            dest.writeString(this.giftPicUrl);
            dest.writeParcelable(this.awardInfo, flags);
            dest.writeInt(this.supportPeopleNum);
            dest.writeInt(this.finishedWishCard);
            dest.writeInt(this.giftPrice);
            dest.writeLong(this.totalGiftWorth);
            dest.writeInt(this.sendGiftPrice);
            dest.writeInt(this.wishGiftNum);
            dest.writeString(this.giftName);
            dest.writeString(this.sendGiftName);
            dest.writeString(this.distributeRule);
            dest.writeInt(this.remainTime);
        }

        public DataBean() {
        }

        protected DataBean(Parcel in) {
            this.totalWishCard = in.readInt();
            this.giftPicUrl = in.readString();
            this.awardInfo = in.readParcelable(AwardInfoBean.class.getClassLoader());
            this.supportPeopleNum = in.readInt();
            this.finishedWishCard = in.readInt();
            this.giftPrice = in.readInt();
            this.totalGiftWorth = in.readLong();
            this.sendGiftPrice = in.readInt();
            this.wishGiftNum = in.readInt();
            this.giftName = in.readString();
            this.sendGiftName = in.readString();
            this.distributeRule = in.readString();
            this.remainTime = in.readInt();
        }

        public static final Parcelable.Creator<DataBean> CREATOR = new Parcelable.Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel source) {
                return new DataBean(source);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };
    }

    public static class AwardInfoBean implements Parcelable {
        /**
         * officalAwardPeopleNumber : -1
         * awardType : RANDOM
         * awardPeopleNumber : 10
         * awardWealth : 20000
         * officalAwardType : ORDINARY
         * officalAwardWealth : 50000
         */

        public int officalAwardPeopleNumber;
        public String awardType;
        public int awardPeopleNumber;
        public long awardWealth;
        public String officalAwardType;
        public long officalAwardWealth;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.officalAwardPeopleNumber);
            dest.writeString(this.awardType);
            dest.writeInt(this.awardPeopleNumber);
            dest.writeLong(this.awardWealth);
            dest.writeString(this.officalAwardType);
            dest.writeLong(this.officalAwardWealth);
        }

        public AwardInfoBean() {
        }

        protected AwardInfoBean(Parcel in) {
            this.officalAwardPeopleNumber = in.readInt();
            this.awardType = in.readString();
            this.awardPeopleNumber = in.readInt();
            this.awardWealth = in.readLong();
            this.officalAwardType = in.readString();
            this.officalAwardWealth = in.readLong();
        }

        public static final Parcelable.Creator<AwardInfoBean> CREATOR = new Parcelable.Creator<AwardInfoBean>() {
            @Override
            public AwardInfoBean createFromParcel(Parcel source) {
                return new AwardInfoBean(source);
            }

            @Override
            public AwardInfoBean[] newArray(int size) {
                return new AwardInfoBean[size];
            }
        };
    }
}
