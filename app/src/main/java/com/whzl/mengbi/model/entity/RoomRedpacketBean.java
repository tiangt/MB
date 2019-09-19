package com.whzl.mengbi.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @author nobody
 * @date 2019-09-11
 */
public class RoomRedpacketBean implements Parcelable {


    public List<ListBean> list;

    public static class ListBean implements Parcelable {
        /**
         * userId : 30000096
         * programId : 100199
         * awardType : GOODS
         * awardGoodsId : 94329
         * awardGoodsName : 静一静
         * awardGoodsNum : 2000
         * awardTotalPrice : 0
         * conditionGoodsId : 94329
         * conditionGoodsName : 静一静
         * conditionGoodsNum : 100
         * conditionPrice : 0
         * awardPeopleNum : 2
         * startTime : 2019-09-06 17:13:35
         * closeTime : 2019-09-06 17:17:35
         * totalValidPeople : 0
         * userIsSatisfied : T
         * nickname : xxxxx
         */

        public int userId;
        public int programId;
        public String awardType;
        public int awardGoodsId;
        public String awardGoodsName;
        public long awardGoodsNum;
        public long awardTotalPrice;
        public int conditionGoodsId;
        public String conditionGoodsName;
        public long conditionGoodsNum;
        public long conditionPrice;
        public long awardPeopleNum;
        public String startTime;
        public String closeTime;
        public long totalValidPeople;
        public String userIsSatisfied;
        public String nickname;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.userId);
            dest.writeInt(this.programId);
            dest.writeString(this.awardType);
            dest.writeInt(this.awardGoodsId);
            dest.writeString(this.awardGoodsName);
            dest.writeLong(this.awardGoodsNum);
            dest.writeLong(this.awardTotalPrice);
            dest.writeInt(this.conditionGoodsId);
            dest.writeString(this.conditionGoodsName);
            dest.writeLong(this.conditionGoodsNum);
            dest.writeLong(this.conditionPrice);
            dest.writeLong(this.awardPeopleNum);
            dest.writeString(this.startTime);
            dest.writeString(this.closeTime);
            dest.writeLong(this.totalValidPeople);
            dest.writeString(this.userIsSatisfied);
            dest.writeString(this.nickname);
        }

        public ListBean() {
        }

        protected ListBean(Parcel in) {
            this.userId = in.readInt();
            this.programId = in.readInt();
            this.awardType = in.readString();
            this.awardGoodsId = in.readInt();
            this.awardGoodsName = in.readString();
            this.awardGoodsNum = in.readLong();
            this.awardTotalPrice = in.readLong();
            this.conditionGoodsId = in.readInt();
            this.conditionGoodsName = in.readString();
            this.conditionGoodsNum = in.readLong();
            this.conditionPrice = in.readLong();
            this.awardPeopleNum = in.readLong();
            this.startTime = in.readString();
            this.closeTime = in.readString();
            this.totalValidPeople = in.readLong();
            this.userIsSatisfied = in.readString();
            this.nickname = in.readString();
        }

        public static final Parcelable.Creator<ListBean> CREATOR = new Parcelable.Creator<ListBean>() {
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
        dest.writeTypedList(this.list);
    }

    public RoomRedpacketBean() {
    }

    protected RoomRedpacketBean(Parcel in) {
        this.list = in.createTypedArrayList(ListBean.CREATOR);
    }

    public static final Parcelable.Creator<RoomRedpacketBean> CREATOR = new Parcelable.Creator<RoomRedpacketBean>() {
        @Override
        public RoomRedpacketBean createFromParcel(Parcel source) {
            return new RoomRedpacketBean(source);
        }

        @Override
        public RoomRedpacketBean[] newArray(int size) {
            return new RoomRedpacketBean[size];
        }
    };
}
