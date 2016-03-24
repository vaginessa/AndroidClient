package com.sc3.securecameracaptureclient;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class gridLayout extends AppCompatActivity {

    private List<Card> cards;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
        cards.add(new Card("", "", R.drawable.nature1));
        cards.add(new Card("", "", R.drawable.nature1));
        cards.add(new Card("", "", R.drawable.nature3));
    }

    private void initializeAdapter(){
        RVAdapter adapter = new RVAdapter(cards, this.getBaseContext());
        rv.setAdapter(adapter);
    }
}
