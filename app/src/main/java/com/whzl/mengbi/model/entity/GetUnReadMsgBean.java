package com.whzl.mengbi.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author nobody
 * @date 2019/3/18
 */
public class GetUnReadMsgBean implements Parcelable {

    public List<ListBean> list;

    public static class ListBean implements Parcelable {
        /**
         * userId : 30000803
         * messageType : GOODS_TYPE
         * messageNum : 1
         */

        public int userId;
        public String messageType;
        public int messageNum;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.userId);
            dest.writeString(this.messageType);
            dest.writeInt(this.messageNum);
        }

        public ListBean() {
        }

        protected ListBean(Parcel in) {
            this.userId = in.readInt();
            this.messageType = in.readString();
            this.messageNum = in.readInt();
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

    public GetUnReadMsgBean() {
    }

    protected GetUnReadMsgBean(Parcel in) {
        this.list = new ArrayList<ListBean>();
        in.readList(this.list, ListBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<GetUnReadMsgBean> CREATOR = new Parcelable.Creator<GetUnReadMsgBean>() {
        @Override
        public GetUnReadMsgBean createFromParcel(Parcel source) {
            return new GetUnReadMsgBean(source);
        }

        @Override
        public GetUnReadMsgBean[] newArray(int size) {
            return new GetUnReadMsgBean[size];
        }
    };
}
