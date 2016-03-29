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
public class DayViewActivity extends AppCompatActivity {

    private List<Card> years;
    private RecyclerView rv;
    ArrayList<Day> dayArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setTitle("Day");

        dayArrayList = this.getIntent().getParcelableArrayListExtra("JSONTREE");

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
        for(int i = 0; i < dayArrayList.size(); i++) {
            years.add(new Card(dayArrayList.get(i).day_name + "", R.drawable.folder));
        }
    }

    private void initializeAdapter(){
        DayAdapter adapter = new DayAdapter(years, dayArrayList, this);
        rv.setAdapter(adapter);
    }
}
