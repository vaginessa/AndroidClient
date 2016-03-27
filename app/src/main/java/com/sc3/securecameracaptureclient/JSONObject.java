package com.sc3.securecameracaptureclient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nathan on 3/27/2016.
 */
public class JSONObject {
    public String id;
    public String date_created;
    public String method;
    public List<Year> year;

    public JSONObject() {
        year = new ArrayList<>();
    }
}