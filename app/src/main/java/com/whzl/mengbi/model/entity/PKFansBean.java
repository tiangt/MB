package com.whzl.mengbi.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author cliang
 * @date 2018.11.13
 */
public class PKFansBean implements Parcelable {

    public long userId;
    public String nickname;
    public String avatar;
    public double score;
    public long lastUpdateTime;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.userId);
        dest.writeString(this.nickname);
        dest.writeString(this.avatar);
        dest.writeDouble(this.score);
        dest.writeLong(this.lastUpdateTime);
    }

    public PKFansBean() {
    }

    protected PKFansBean(Parcel in) {
        this.userId = in.readLong();
        this.nickname = in.readString();
        this.avatar = in.readString();
        this.score = in.readDouble();
        this.lastUpdateTime = in.readLong();
    }

    public static final Parcelable.Creator<PKFansBean> CREATOR = new Parcelable.Creator<PKFansBean>() {
        @Override
        public PKFansBean createFromParcel(Parcel source) {
            return new PKFansBean(source);
        }

        @Override
        public PKFansBean[] newArray(int size) {
            return new PKFansBean[size];
        }
    };
}

