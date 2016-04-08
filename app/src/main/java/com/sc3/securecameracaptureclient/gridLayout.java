package com.sc3.securecameracaptureclient;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class gridLayout extends AppCompatActivity {

    private List<imageHolder> cards;
    private RecyclerView rv;
    ArrayList<Image> ImageArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setTitle("Images");

        ImageArrayList = this.getIntent().getParcelableArrayListExtra("JSONTREE");

        setContentView(R.layout.recycler_view);

        rv=(RecyclerView)findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        initializeData();
        initializeAdapter();

    }

    private void initializeData(){
        cards = new ArrayList<>();

        for(int i = 0; i < ImageArrayList.size(); i ++) {
            cards.add(new imageHolder(getYearMonthString(ImageArrayList.get(i).date_taken),
                    getDayHourString(ImageArrayList.get(i).date_taken), R.drawable.nature1, ImageArrayList.get(i).file_name));
            //YYYY MM DD HH MM
        }
    }

    private String getYearMonthString(String date) {
        String ret;
        ret = getMonthString(Integer.parseInt(date.substring(4,6)));
        ret += " " + date.substring(6,8);
        ret += " " + date.substring(0,4);
        return ret;
    }

    private String getDayHourString(String date) {
        String ret;
        ret = formatHour(Integer.parseInt(date.substring(8, 10)), Integer.parseInt(date.substring(10)));
        return ret;
    }

    private void initializeAdapter(){
        RVAdapter adapter = new RVAdapter(cards, this.getBaseContext());
        rv.setAdapter(adapter);
    }

    public void onBackPressed(){
        finish();
    }

    private String formatHour(int hour, int minute) {

        if(hour<12) {
            if(hour==0) {
                hour=12;
            }
            return (hour + ":" + minute + " am");
        } else {
            hour-=12;
            return (hour + ":" + minute + " pm");
        }

    }

    public String getMonthString(int month) {
        switch (month) {
            case 1:
                return "January";
            case 2:
                return "February";
            case 3:
                return "March";
            case 4:
                return "April";
            case 5:
                return "May";
            case 6:
                return "June";
            case 7:
                return "July";
            case 8:
                return "August";
            case 9:
                return "September";
            case 10:
                return "October";
            case 11:
                return "November";
            case 12:
                return "December";
            default:
                return "";
        }
    }
}
