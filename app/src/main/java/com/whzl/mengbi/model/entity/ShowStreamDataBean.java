package com.whzl.mengbi.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author nobody
 * @date 2019-09-06
 */
public class ShowStreamDataBean implements Parcelable {
    /**
     * height : 540
     * width : 960
     * liveType : PC
     * rtmp : rtmp://livedown.mengbitv.com/live/100163
     * flv : https://livedown.mengbitv.com/live/100163.flv
     * hls : http://livedown.mengbitv.com/live/100163/playlist.m3u8
     */

    public int height;
    public int width;
    public String liveType;
    public String rtmp;
    public String flv;
    public String hls;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.height);
        dest.writeInt(this.width);
        dest.writeString(this.liveType);
        dest.writeString(this.rtmp);
        dest.writeString(this.flv);
        dest.writeString(this.hls);
    }

    public ShowStreamDataBean() {
    }

    protected ShowStreamDataBean(Parcel in) {
        this.height = in.readInt();
        this.width = in.readInt();
        this.liveType = in.readString();
        this.rtmp = in.readString();
        this.flv = in.readString();
        this.hls = in.readString();
    }

    public static final Parcelable.Creator<ShowStreamDataBean> CREATOR = new Parcelable.Creator<ShowStreamDataBean>() {
        @Override
        public ShowStreamDataBean createFromParcel(Parcel source) {
            return new ShowStreamDataBean(source);
        }

        @Override
        public ShowStreamDataBean[] newArray(int size) {
            return new ShowStreamDataBean[size];
        }
    };
}
