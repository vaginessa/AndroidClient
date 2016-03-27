package com.sc3.securecameracaptureclient;

import java.util.ArrayList;

/**
 * Created by Nathan on 3/27/2016.
 */
public class Hour
{
    public Hour(int hour)
    {
        this.hour = hour;
        this.images = new ArrayList<>();
    }
    //This is the hour in the day, 0-23
    public int hour;
    public ArrayList<Image> images;
}
