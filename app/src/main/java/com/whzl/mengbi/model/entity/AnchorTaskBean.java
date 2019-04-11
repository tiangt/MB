package com.whzl.mengbi.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author nobody
 * @date 2018/11/29
 */
public class AnchorTaskBean implements Parcelable {


    /**
     * taskId : 23090
     * completion : 0
     * needCompletion : 10
     * operation :
     * number : 0
     * unit :
     * pic : https://localtest-img.mengbitv.com/app-img/activity/anchor-week-task/23090.png
     * detailPic : https://localtest-img.mengbitv.com/app-img/activity/anchor-week-task/detail_23090.png
     * time : 每周一00:00:00-23:59:59
     * awardList : [{"awardPic":"http://localtest-img.mengbitv.com/default/000/00/05/92_144x144.jpg","awardNum":2},{"awardPic":"http://localtest-img.mengbitv.com/default/000/00/05/91_144x144.jpg","awardNum":2}]
     */

    public int taskId;
    public int completion;
    public int needCompletion;
    public String operation;
    public int number;
    public String unit;
    public String pic;
    public String detailPic;
    public String time;
    public String name;
    public List<AwardListBean> awardList;

    public static class AwardListBean implements Parcelable {
        /**
         * awardPic : http://localtest-img.mengbitv.com/default/000/00/05/92_144x144.jpg
         * awardNum : 2
         */

        public String awardPic;
        public int awardNum;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.awardPic);
            dest.writeInt(this.awardNum);
        }

        public AwardListBean() {
        }

        protected AwardListBean(Parcel in) {
            this.awardPic = in.readString();
            this.awardNum = in.readInt();
        }

        public static final Creator<AwardListBean> CREATOR = new Creator<AwardListBean>() {
            @Override
            public AwardListBean createFromParcel(Parcel source) {
                return new AwardListBean(source);
            }

            @Override
            public AwardListBean[] newArray(int size) {
                return new AwardListBean[size];
            }
        };
    }

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
        dest.writeString(this.detailPic);
        dest.writeString(this.time);
        dest.writeList(this.awardList);
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
        this.detailPic = in.readString();
        this.time = in.readString();
        this.awardList = new ArrayList<AwardListBean>();
        in.readList(this.awardList, AwardListBean.class.getClassLoader());
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
