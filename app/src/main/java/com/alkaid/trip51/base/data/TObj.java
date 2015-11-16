package com.alkaid.trip51.base.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alkaid on 2015/11/16.
 */
public class TObj implements Parcelable{
    protected TObj(Parcel in) {
    }

    public static final Creator<TObj> CREATOR = new Creator<TObj>() {
        @Override
        public TObj createFromParcel(Parcel in) {
            return new TObj(in);
        }

        @Override
        public TObj[] newArray(int size) {
            return new TObj[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }
}
