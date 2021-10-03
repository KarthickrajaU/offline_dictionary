package com.example.aspirantmod5;

import android.database.Cursor;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import static android.R.layout.simple_list_item_1;

public class saved_view extends AppCompatActivity {
    static databasehelper mydbhelp;
    static boolean databaseOpened = false;


    SimpleCursorAdapter adapter;

    ArrayList<savedlist> saved;
    RecyclerView recycler_view_saved;
    RecyclerView.LayoutManager layoutManager;
    RecyclerView.Adapter savedAdapter;

    RelativeLayout emptysaved;
    Cursor c = null;
    private String en_word;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_view);
        TextView tamil=findViewById(R.id.tamil);
        mydbhelp = new databasehelper(this);
        //

        fetch_saved();

    }


    private void fetch_saved() {
        emptysaved = findViewById(R.id.empty_saved);

        //recycler View
        recycler_view_saved = findViewById(R.id.recycler_view_saved);
        layoutManager = new LinearLayoutManager(saved_view.this);
      //  savedAdapter=new savedrecycle(this,getallitems());

        recycler_view_saved.setLayoutManager(layoutManager);
        //  mydbhelp = new databasehelper(this);
        mydbhelp = new databasehelper(this);
        saved = new ArrayList<>();
        savedAdapter = new savedrecycle(this, saved);
        recycler_view_saved.setAdapter(savedAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                remove_saved_word((String) viewHolder.itemView.getTag());
                //savedAdapter.notifyDataSetChanged();
              //  remove_saved_word((String) viewHolder.itemView.getTag());
            }
        }).attachToRecyclerView(recycler_view_saved);

        savedlist s;

        if (databaseOpened) {
            mydbhelp = new databasehelper(this);
            c = mydbhelp.getsaved();
            if (c.moveToFirst()) {
                do {
                    s = new savedlist(c.getString(c.getColumnIndex("word")),c.getString(c.getColumnIndex("ex")), c.getString(c.getColumnIndex("tami")));
                    saved.add(s);
                }
                while (c.moveToNext());
            }

            savedAdapter.notifyDataSetChanged();


            if (savedAdapter.getItemCount() == 0) {
                emptysaved.setVisibility(View.VISIBLE);
            } else {
                emptysaved.setVisibility(View.GONE);

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

//SimpleCursorAdapter adapter;
    private void remove_saved_word(String en_word) {

        mydbhelp.delete_saved_word(en_word);

        Toast.makeText(getApplicationContext(),  en_word+ " deleted from Saved List",Toast.LENGTH_SHORT).show();
        fetch_saved();
      savedAdapter.notifyDataSetChanged();
       // adapter.swapCursor(c);
//        adapter.swapCursor(c);
       //fetch_saved();
    }

    @Override
    protected void onStart(){
        super.onStart();
        fetch_saved();
    }
    @Override
    protected void onResume(){
        super.onResume();
        fetch_saved();
    }
}








