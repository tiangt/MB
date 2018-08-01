package com.whzl.mengbi.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * @author shaw
 * @date 2018/7/31
 */
public class BackpackListBean {

    public List<GoodsDetailBean> list;

    public class GoodsDetailBean implements Parcelable{
        /**
         * goodsId : 94334
         * goodsName : 亲一亲
         * goodsPic : http://test-img.mengbitv.com/default/000/00/01/23.jpg
         * count : 5
         */

        public int goodsId;
        public String goodsName;
        public String goodsPic;
        public int count;

        protected GoodsDetailBean(Parcel in) {
            goodsId = in.readInt();
            goodsName = in.readString();
            goodsPic = in.readString();
            count = in.readInt();
        }

        public final Creator<GoodsDetailBean> CREATOR = new Creator<GoodsDetailBean>() {
            @Override
            public GoodsDetailBean createFromParcel(Parcel in) {
                return new GoodsDetailBean(in);
            }

            @Override
            public GoodsDetailBean[] newArray(int size) {
                return new GoodsDetailBean[size];
            }
        };

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(goodsId);
            dest.writeString(goodsName);
            dest.writeString(goodsPic);
            dest.writeInt(count);
        }
    }
}
