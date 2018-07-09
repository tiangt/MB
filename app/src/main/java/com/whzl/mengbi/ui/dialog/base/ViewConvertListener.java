package com.whzl.mengbi.ui.dialog.base;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author shaw
 */
public abstract class ViewConvertListener implements Parcelable {

    protected abstract void convertView(ViewHolder holder, BaseAwesomeDialog dialog);

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public ViewConvertListener() {
    }

    protected ViewConvertListener(Parcel in) {
    }

    public static final Creator<ViewConvertListener> CREATOR = new Creator<ViewConvertListener>() {
        @Override
        public ViewConvertListener createFromParcel(Parcel source) {
            return new ViewConvertListener(source){
                @Override
                protected void convertView(ViewHolder holder, BaseAwesomeDialog dialog) {

                }
            };
        }

        @Override
        public ViewConvertListener[] newArray(int size) {
            return new ViewConvertListener[size];
        }
    };
}
