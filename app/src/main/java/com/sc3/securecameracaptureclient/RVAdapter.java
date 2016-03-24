package com.sc3.securecameracaptureclient;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nathan on 3/24/2016.
 */
public class RVAdapter extends RecyclerView.Adapter<RVAdapter.CardViewHolder> {

    public static class CardViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        ImageView photo;

        CardViewHolder(View itemView) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            photo = (ImageView) itemView.findViewById(R.id.iv);

            assert cv != null;
            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("CLICKED", "CardView clicked");
                }
            });
        }
    }

    List<Card> cards;
    Context context;
    ArrayList<Integer> drawables = new ArrayList<>();

    RVAdapter(List<Card> cards, Context context){
        this.cards = cards;
        this.context = context;
        drawables.add(R.drawable.nature1);
        drawables.add(R.drawable.nature2);
        drawables.add(R.drawable.nature3);
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view, viewGroup, false);
        CardViewHolder pvh = new CardViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(CardViewHolder cardViewHolder, int i) {
        //cardViewHolder.personName.setText(persons.get(i).name);
        //cardViewHolder.personAge.setText(persons.get(i).age);
        assert cardViewHolder.photo != null;
        Picasso.with(cardViewHolder.photo.getContext())
                .load(drawables.get(i))
                .resize(dp2px(220), 0)
                .into(cardViewHolder.photo);
    }

    public int dp2px(int dp) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        DisplayMetrics displaymetrics = new DisplayMetrics();
        display.getMetrics(displaymetrics);
        return (int) (dp * displaymetrics.density + 0.5f);
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }
}
