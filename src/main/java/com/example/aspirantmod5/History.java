package com.example.aspirantmod5;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.database.CursorWindow;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;


public class History extends AppCompatActivity {
    static databasehelper mydbhelp;
    static boolean databaseOpened=false;

    SimpleCursorAdapter simpleCursorAdapter;

    ArrayList<history_List> historylist;
    RecyclerView recycler_view_history;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter historyAdapter;

    RelativeLayout emptyHistory;
    Cursor c;
    private String en_word;
    private String definition;
    private String tamil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        mydbhelp = new databasehelper(this);
        //
        fetch_history();

    }
    private void fetch_history() {

        emptyHistory = findViewById(R.id.empty_history);

        //recycler View
        recycler_view_history = findViewById(R.id.recycler_view_history);
        layoutManager = new LinearLayoutManager(History.this);

        recycler_view_history.setLayoutManager(layoutManager);
        //  mydbhelp = new databasehelper(this);
        mydbhelp = new databasehelper(this);
        historylist = new ArrayList<history_List>();
        historyAdapter = new RecyclerViewAdapterHistory(this, historylist);
        recycler_view_history.setAdapter(historyAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
               // remove_history_word((Long) viewHolder.itemView.getTag());
                //savedAdapter.notifyDataSetChanged();
                //  remove_saved_word((String) viewHolder.itemView.getTag());
            remove_history_word((String) viewHolder.itemView.getTag());
            }
        }).attachToRecyclerView(recycler_view_history);

        history_List h;

        if (databaseOpened) {
            mydbhelp = new databasehelper(this);
            c = mydbhelp.getHistory();
            if (c.moveToFirst()) {
                do {
                    h = new history_List(c.getString(c.getColumnIndex("word")),c.getString(c.getColumnIndex("tami")),c.getString(c.getColumnIndex("ex")));
                    historylist.add(h);
                }
                while (c.moveToNext());
            }

            historyAdapter.notifyDataSetChanged();


            if (historyAdapter.getItemCount() == 0) {
                emptyHistory.setVisibility(View.VISIBLE);
            } else {
                emptyHistory.setVisibility(View.GONE);
            }


        }
        openDatabase();
    }



    public void openDatabase() {
        mydbhelp = new databasehelper(this);
        try {
            mydbhelp.openDatabase();
            databaseOpened = true;

        } catch (SQLException e) {

            e.printStackTrace();
        }
    }
    private void remove_history_word(String en_word) {
        mydbhelp.delete_history_word(en_word);
        Toast.makeText(getApplicationContext(),  en_word+ " deleted from History List",Toast.LENGTH_SHORT).show();
     //   simpleCursorAdapter.swapCursor(c);
        fetch_history();
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        fetch_history();
    }
    protected void onResume(){
        super.onResume();

    }

    @Override
    protected void onStart() {
        super.onStart();
        fetch_history();
    }
}
