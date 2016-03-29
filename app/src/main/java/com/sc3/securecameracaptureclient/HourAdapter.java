package com.sc3.securecameracaptureclient;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Nathan on 3/29/2016.
 */
public class HourAdapter extends RecyclerView.Adapter<HourAdapter.HourCardViewHolder> {

    public static class HourCardViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView cardName;
        ImageView cardPhoto;
        int itemIndex;

        HourCardViewHolder(View itemView, final Context c) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            cardName = (TextView)itemView.findViewById(R.id.card_name);
            cardPhoto = (ImageView)itemView.findViewById(R.id.card_photo);

            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //headed to day
                    Intent yearIntent = new Intent(c, gridLayout.class);
                    yearIntent.putParcelableArrayListExtra("JSONTREE", hours.get(itemIndex).images);
                    c.startActivity(yearIntent);
                }
            });
        }
    }

    List<Card> persons;
    static ArrayList<Hour> hours;
    Context c;

    HourAdapter(List<Card> persons, ArrayList<Hour> hours, Context c){
        this.persons = persons;
        this.hours = hours;
        this.c = c;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public HourCardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.directory_card_view, viewGroup, false);
        HourCardViewHolder pvh = new HourCardViewHolder(v, c);
        return pvh;
    }

    @Override
    public void onBindViewHolder(HourCardViewHolder personViewHolder, int i) {
        personViewHolder.cardName.setText(persons.get(i).name);
        personViewHolder.cardPhoto.setImageResource(persons.get(i).photoId);
        personViewHolder.itemIndex = i;
    }

    @Override
    public int getItemCount() {
        return persons.size();
    }
}