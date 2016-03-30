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
            cards.add(new imageHolder(ImageArrayList.get(i).date_taken + "", ImageArrayList.get(i).file_name + "", R.drawable.nature1));
        }
    }

    private void initializeAdapter(){
        RVAdapter adapter = new RVAdapter(cards, this.getBaseContext());
        rv.setAdapter(adapter);
    }
}
