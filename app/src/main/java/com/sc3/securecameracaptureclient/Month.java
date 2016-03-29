package com.sc3.securecameracaptureclient;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Nathan on 3/27/2016.
 */
public class Month implements Parcelable {
    public Month(int month_name)
    {
        this.month_name = month_name;
        this.days = new ArrayList<>();
    }
    //This is 1-12 January-December Respective
    public int month_name;
    public ArrayList<Day> days;


    protected Month(Parcel in) {
        month_name = in.readInt();
        if (in.readByte() == 0x01) {
            days = new ArrayList<Day>();
            in.readList(days, Day.class.getClassLoader());
        } else {
            days = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(month_name);
        if (days == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(days);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Month> CREATOR = new Parcelable.Creator<Month>() {
        @Override
        public Month createFromParcel(Parcel in) {
            return new Month(in);
        }

        @Override
        public Month[] newArray(int size) {
            return new Month[size];
        }
    };
}
