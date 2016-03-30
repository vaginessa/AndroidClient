package com.sc3.securecameracaptureclient;

import android.content.Context;
import android.content.Intent;
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
        TextView title;
        TextView subTitle;

        CardViewHolder(View itemView, final Context c) {
            super(itemView);
            cv = (CardView) itemView.findViewById(R.id.cv);
            photo = (ImageView) itemView.findViewById(R.id.iv);
            title = (TextView) itemView.findViewById(R.id.image_card_title);
            subTitle = (TextView) itemView.findViewById(R.id.image_card_subTitle);

            assert cv != null;
            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent yearIntent = new Intent(c, ImageViewActivity.class);
                    yearIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    yearIntent.putExtra("picture", subTitle.getText().toString());
                    c.startActivity(yearIntent);
                }
            });
        }
    }

    List<imageHolder> cards;
    Context context;
    ArrayList<Integer> drawables = new ArrayList<>();

    RVAdapter(List<imageHolder> cards, Context context){
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
        CardViewHolder pvh = new CardViewHolder(v, context);
        return pvh;
    }

    @Override
    public void onBindViewHolder(CardViewHolder cardViewHolder, int i) {
        //cardViewHolder.personName.setText(persons.get(i).name);
        //cardViewHolder.personAge.setText(persons.get(i).age);
        assert cardViewHolder.photo != null;
        Picasso.with(cardViewHolder.photo.getContext())
                .load(drawables.get(0))
                .resize(dp2px(220), 0)
                .into(cardViewHolder.photo);
        cardViewHolder.title.setText(cards.get(i).name);
        cardViewHolder.subTitle.setText(cards.get(i).age);
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
