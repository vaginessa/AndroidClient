package com.sc3.securecameracaptureclient;

/**
 * Created by Nathan on 3/27/2016.
 */
public class Image
{
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
}
