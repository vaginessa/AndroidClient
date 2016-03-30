package com.sc3.securecameracaptureclient;

/**
 * Created by Nathan on 3/29/2016.
 */

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nathan on 3/29/2016.
 */
public class MonthViewActivity extends AppCompatActivity {

    private List<Card> years;
    private RecyclerView rv;
    ArrayList<Month> monthArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setTitle("Month");

        monthArrayList = this.getIntent().getParcelableArrayListExtra("JSONTREE");

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
        for(int i = 0; i < monthArrayList.size(); i++) {
            years.add(new Card(getMonthString(monthArrayList.get(i).month_name), R.drawable.folder));
        }
    }

    private void initializeAdapter(){
        MonthAdapter adapter = new MonthAdapter(years, monthArrayList, this);
        rv.setAdapter(adapter);
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

