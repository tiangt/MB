package com.whzl.mengbi.model.entity

import android.os.Parcel
import android.os.Parcelable

/**
 * @author nobody
 * @date 2019-07-02
 */
data class RoyalCarListBean(val list: List<X>) : Parcelable {
    constructor(source: Parcel) : this(
            ArrayList<X>().apply { source.readList(this, X::class.java.classLoader) }
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeList(list)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<RoyalCarListBean> = object : Parcelable.Creator<RoyalCarListBean> {
            override fun createFromParcel(source: Parcel): RoyalCarListBean = RoyalCarListBean(source)
            override fun newArray(size: Int): Array<RoyalCarListBean?> = arrayOfNulls(size)
        }
    }
}

data class X(val carImageUrl: String,
             val royalLevel: Int) : Parcelable {
    constructor(source: Parcel) : this(
            source.readString(),
            source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(carImageUrl)
        writeInt(royalLevel)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<X> = object : Parcelable.Creator<X> {
            override fun createFromParcel(source: Parcel): X = X(source)
            override fun newArray(size: Int): Array<X?> = arrayOfNulls(size)
        }
    }
}