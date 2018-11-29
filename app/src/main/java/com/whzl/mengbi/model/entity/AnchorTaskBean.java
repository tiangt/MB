package com.whzl.mengbi.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author nobody
 * @date 2018/11/29
 */
public class AnchorTaskBean implements Parcelable {


    /**
     * taskId : 23093
     * completion  : 0
     * needCompletion : 30
     * operation :
     * number : 0
     * unit :
     * pic : https://test-img.mengbitv.com/api-img/activity/anchor-week-task/23093.png
     */

    public int taskId;
    public int completion;
    public int needCompletion;
    public String operation;
    public int number;
    public String unit;
    public String pic;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.taskId);
        dest.writeInt(this.completion);
        dest.writeInt(this.needCompletion);
        dest.writeString(this.operation);
        dest.writeInt(this.number);
        dest.writeString(this.unit);
        dest.writeString(this.pic);
    }

    public AnchorTaskBean() {
    }

    protected AnchorTaskBean(Parcel in) {
        this.taskId = in.readInt();
        this.completion = in.readInt();
        this.needCompletion = in.readInt();
        this.operation = in.readString();
        this.number = in.readInt();
        this.unit = in.readString();
        this.pic = in.readString();
    }

    public static final Parcelable.Creator<AnchorTaskBean> CREATOR = new Parcelable.Creator<AnchorTaskBean>() {
        @Override
        public AnchorTaskBean createFromParcel(Parcel source) {
            return new AnchorTaskBean(source);
        }

        @Override
        public AnchorTaskBean[] newArray(int size) {
            return new AnchorTaskBean[size];
        }
    };
}
