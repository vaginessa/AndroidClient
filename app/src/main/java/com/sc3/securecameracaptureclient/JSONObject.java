package com.sc3.securecameracaptureclient;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nathan on 3/27/2016.
 */
public class JSONObject{
    public String id;
    public String date_created;
    public String method;
    public ArrayList<Year> year;

    public JSONObject() {
        year = new ArrayList<>();
    }

}