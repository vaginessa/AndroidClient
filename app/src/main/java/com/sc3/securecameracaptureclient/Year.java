package com.sc3.securecameracaptureclient;

import java.util.ArrayList;

/**
 * Created by Nathan on 3/27/2016.
 */
 public class Year
 {
     public Year(int year_name) {
         this.year_name = year_name;
         this.months = new ArrayList<>();
     }

     //Int value of the year such as 2016
     public int year_name;
     public ArrayList<Month> months;
 }
