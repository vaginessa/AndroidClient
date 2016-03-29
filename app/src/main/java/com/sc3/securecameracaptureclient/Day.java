package com.sc3.securecameracaptureclient;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Nathan on 3/27/2016.
 */
public class Day implements Parcelable {
    public Day(int day_name)
    {
        this.day_name = day_name;
        this.hours = new ArrayList<>();
    }
    //This is the number of the day in the month 1-31ish
    public int day_name;
    public ArrayList<Hour> hours;

    protected Day(Parcel in) {
        day_name = in.readInt();
        if (in.readByte() == 0x01) {
            hours = new ArrayList<Hour>();
            in.readList(hours, Hour.class.getClassLoader());
        } else {
            hours = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(day_name);
        if (hours == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(hours);
        }
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Day> CREATOR = new Parcelable.Creator<Day>() {
        @Override
        public Day createFromParcel(Parcel in) {
            return new Day(in);
        }

        @Override
        public Day[] newArray(int size) {
            return new Day[size];
        }
    };
}
