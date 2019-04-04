package com.whzl.mengbi.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @author shaw
 * @date 2018/7/31
 */
public class BackpackListBean implements Parcelable {

    public List<GoodsDetailBean> list;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.list);
    }

    public BackpackListBean() {
    }

    protected BackpackListBean(Parcel in) {
        this.list = in.createTypedArrayList(GoodsDetailBean.CREATOR);
    }

    public static final Parcelable.Creator<BackpackListBean> CREATOR = new Parcelable.Creator<BackpackListBean>() {
        @Override
        public BackpackListBean createFromParcel(Parcel source) {
            return new BackpackListBean(source);
        }

        @Override
        public BackpackListBean[] newArray(int size) {
            return new BackpackListBean[size];
        }
    };
}
