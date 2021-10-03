package com.example.aspirantmod5;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;

import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;


public class RecyclerViewAdapterHistory extends RecyclerView.Adapter<RecyclerViewAdapterHistory.HistoryViewHolder> {

    private final ArrayList<history_List> histories;
    private final Context context;

    public RecyclerViewAdapterHistory(Context context, ArrayList<history_List> histories) {
        this.histories = histories;
        this.context = context;
    }

    public  class HistoryViewHolder extends RecyclerView.ViewHolder {
        TextView en_word;
        TextView en_def;
        long id;
        TextView tamil;
        int click;


        public HistoryViewHolder(View v) {
            super(v);
            en_word = v.findViewById(R.id.en_word);
            en_def = v.findViewById(R.id.en_def);
            tamil=v.findViewById(R.id.tamil);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click=click+1;
                    click=click%2;
                    if(click == 0) {
                        tamil.setVisibility(View.GONE);
                    }
                    else
                    {tamil.setVisibility(View.VISIBLE); }//open_menu();
                }

            });
        }
    }


    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent,int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_layout, parent, false);
        return new HistoryViewHolder(view);
    }


    @Override
    public void onBindViewHolder(HistoryViewHolder holder, final int position) {
        holder.en_word.setText(histories.get(position).get_en_word());
        holder.en_def.setText(histories.get(position).get_def());
        holder.tamil.setText("def:" +histories.get(position).get_tamil());
        holder.itemView.setTag(histories.get(position).get_en_word());
    }

    @Override
    public int getItemCount() {
        return histories.size();
    }
}