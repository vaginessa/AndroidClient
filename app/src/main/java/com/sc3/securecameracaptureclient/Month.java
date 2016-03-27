package com.sc3.securecameracaptureclient;

import java.util.ArrayList;

/**
 * Created by Nathan on 3/27/2016.
 */
public class Month
{
    public Month(int month_name)
    {
        this.month_name = month_name;
        this.days = new ArrayList<>();
    }
    //This is 1-12 January-December Respective
    public int month_name;
    public ArrayList<Day> days;

}
