package com.example.aspirantmod5;


import android.app.StatusBarManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static android.app.PendingIntent.getActivity;
import static com.example.aspirantmod5.saved_view.mydbhelp;

public class savedrecycle extends RecyclerView.Adapter<savedrecycle.HistoryViewHolder> {

    private final ArrayList<savedlist> saved;
    private final Context context;
    Cursor c;
    int a=0;
boolean clicked=false;
    public savedrecycle(Context context, ArrayList<savedlist> saved) {
        this.saved = saved;
        this.context = context;
      //  notifyDataSetChanged();
    }



    public  class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView en_word;
        TextView en_def;
        TextView tamil;
        int click=0;

       // tamil = v.findViewById(R.id.tamil);

        public HistoryViewHolder(View v) {
            super(v);
            en_word = v.findViewById(R.id.en_word);
            en_def = v.findViewById(R.id.en_def);
            tamil = v.findViewById(R.id.tamil);
           en_def.setVisibility(View.GONE);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click=click+1;
                    click=click%2;
                    if(click == 0) {
                        en_def.setVisibility(View.GONE);
                    }
                    else
                    {en_def.setVisibility(View.VISIBLE); }//open_menu();
                }

            });

        }}



    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    //notifyDataSetChanged();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_item_layout, parent, false);
   //    notifyDataSetChanged();
        return new HistoryViewHolder(view);
    }


    @Override
    public void onBindViewHolder(HistoryViewHolder holder, final int position) {
       // notifyDataSetChanged();
        holder.en_def.setText("def:"+saved.get(position).get_def());
        holder.en_word.setText(saved.get(position).get_en_word());
        holder.tamil.setText(saved.get(position).get_tamil());
        holder.itemView.setTag(saved.get(position).get_en_word());
       // holder.tamil.setVisibility(View.);
      //  holder.en_word.setTag(saved.get(position).get_en_word());
    }

    @Override
    public int getItemCount() {
        return saved.size();
    }

}
