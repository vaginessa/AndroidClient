package com.sc3.securecameracaptureclient;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nathan on 3/29/2016.
 */
public class HourViewActivity extends AppCompatActivity {

    private List<Card> years;
    private RecyclerView rv;
    ArrayList<Hour> hourArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setTitle("Hour");

        hourArrayList = this.getIntent().getParcelableArrayListExtra("JSONTREE");

        setContentView(R.layout.recycler_view);

        rv=(RecyclerView)findViewById(R.id.rv);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        initializeData();
        initializeAdapter();
    }

    private void initializeData(){
        years = new ArrayList<>();
        for(int i = 0; i < hourArrayList.size(); i++) {
            years.add(new Card(formatHour(hourArrayList.get(i).hour), R.drawable.folder));
        }
    }

    private void initializeAdapter(){
        HourAdapter adapter = new HourAdapter(years, hourArrayList, this);
        rv.setAdapter(adapter);
    }

    private String formatHour(int hour) {

        if(hour<12) {
            if(hour==0) {
                hour=12;
            }
            return hour + ":00 am";
        } else {
            hour-=12;
            return hour + ":00 pm";
        }
    }
}
