package com.sc3.securecameracaptureclient;

import java.util.ArrayList;

/**
 * Created by Nathan on 3/27/2016.
 */
public class Day
{
    public Day(int day_name)
    {
        this.day_name = day_name;
        this.hours = new ArrayList<>();
    }
    //This is the number of the day in the month 1-31ish
    public int day_name;
    public ArrayList<Hour> hours;
}
