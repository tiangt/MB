package com.whzl.mengbi.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author nobody
 * @date 2019/4/4
 */
public class GoodsDetailBean implements Parcelable {
    /**
     * goodsId : 94334
     * goodsName : 亲一亲
     * goodsPic : http://test-img.mengbitv.com/default/000/00/01/23.jpg
     * count : 5
     */

    public int goodsId;
    public String goodsName;
    public String goodsPic;
    public String goodsType;
    public int count;


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.goodsId);
        dest.writeString(this.goodsName);
        dest.writeString(this.goodsPic);
        dest.writeString(this.goodsType);
        dest.writeInt(this.count);
    }

    public GoodsDetailBean() {
    }

    protected GoodsDetailBean(Parcel in) {
        this.goodsId = in.readInt();
        this.goodsName = in.readString();
        this.goodsPic = in.readString();
        this.goodsType = in.readString();
        this.count = in.readInt();
    }

    public static final Creator<GoodsDetailBean> CREATOR = new Creator<GoodsDetailBean>() {
        @Override
        public GoodsDetailBean createFromParcel(Parcel source) {
            return new GoodsDetailBean(source);
        }

        @Override
        public GoodsDetailBean[] newArray(int size) {
            return new GoodsDetailBean[size];
        }
    };
}