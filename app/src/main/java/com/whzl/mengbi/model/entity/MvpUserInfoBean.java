package com.whzl.mengbi.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author nobody
 * @date 2019-08-14
 */
public class MvpUserInfoBean implements Parcelable {
    /**
     * nickname : 小猪佩奇就是我啊
     * userId : 30000139
     * lastUpdateTime : 2019-06-03 19:46:13
     * score : null
     */

    public String nickname;
    public int userId;
    public String lastUpdateTime;
    public long score;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.nickname);
        dest.writeInt(this.userId);
        dest.writeString(this.lastUpdateTime);
        dest.writeLong(this.score);
    }

    public MvpUserInfoBean() {
    }

    protected MvpUserInfoBean(Parcel in) {
        this.nickname = in.readString();
        this.userId = in.readInt();
        this.lastUpdateTime = in.readString();
        this.score = in.readLong();
    }

    public static final Parcelable.Creator<MvpUserInfoBean> CREATOR = new Parcelable.Creator<MvpUserInfoBean>() {
        @Override
        public MvpUserInfoBean createFromParcel(Parcel source) {
            return new MvpUserInfoBean(source);
        }

        @Override
        public MvpUserInfoBean[] newArray(int size) {
            return new MvpUserInfoBean[size];
        }
    };
}
