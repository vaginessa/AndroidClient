package com.sc3.securecameracaptureclient;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Nathan on 3/27/2016.
 */
public class Year implements Parcelable {
    public Year(int year_name) {
        this.year_name = year_name;
        this.months = new ArrayList<>();
    }

    //Int value of the year such as 2016
    public int year_name;
    public ArrayList<Month> months;

    protected Year(Parcel in) {
        year_name = in.readInt();
        if (in.readByte() == 0x01) {
            months = new ArrayList<Month>();
            in.readList(months, Month.class.getClassLoader());
        } else {
            months = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(year_name);
        if (months == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(months);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Year> CREATOR = new Parcelable.Creator<Year>() {
        @Override
        public Year createFromParcel(Parcel in) {
            return new Year(in);
        }

        @Override
        public Year[] newArray(int size) {
            return new Year[size];
        }
    };
}
