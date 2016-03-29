package com.sc3.securecameracaptureclient;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Nathan on 3/27/2016.
 */
public class Image implements Parcelable {
    public Image(int minute, String file_name, String date_taken, int method)
    {
        this.minute = minute;
        this.file_name = file_name;
        this.date_taken = date_taken;
        this.method = method;
    }
    public int minute;
    public String file_name;
    public String date_taken;
    public int method;

    protected Image(Parcel in) {
        minute = in.readInt();
        file_name = in.readString();
        date_taken = in.readString();
        method = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(minute);
        dest.writeString(file_name);
        dest.writeString(date_taken);
        dest.writeInt(method);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Image> CREATOR = new Parcelable.Creator<Image>() {
        @Override
        public Image createFromParcel(Parcel in) {
            return new Image(in);
        }

        @Override
        public Image[] newArray(int size) {
            return new Image[size];
        }
    };
}
