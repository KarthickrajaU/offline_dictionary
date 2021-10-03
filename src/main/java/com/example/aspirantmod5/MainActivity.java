package com.example.aspirantmod5;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorWindow;
import android.database.SQLException;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Locale;

import static android.speech.RecognizerIntent.ACTION_RECOGNIZE_SPEECH;
import static android.speech.RecognizerIntent.EXTRA_LANGUAGE;
import static android.speech.RecognizerIntent.EXTRA_LANGUAGE_MODEL;
import static android.speech.RecognizerIntent.EXTRA_PROMPT;
import static android.speech.RecognizerIntent.EXTRA_RESULTS;
import static android.speech.RecognizerIntent.LANGUAGE_MODEL_FREE_FORM;
import static android.view.View.VISIBLE;
import static android.widget.Toast.LENGTH_SHORT;
import static android.widget.Toast.makeText;


public class MainActivity extends AppCompatActivity {
    //tamildes,eng_des,tamilword
    //button-search
    //searchbar-searchbar

    static databasehelper mydbhelp;
    static boolean databaseOpened = false;
    String text;
    //DatabaseHelper dbhelper;
    EditText searchbar;
    SimpleCursorAdapter simpleCursorAdapter;
    Button search;
    ImageButton historybtn;
    ImageButton savedbtn;
    ImageButton setting;
    TextView tamil_des;
    TextView eng_des;
    TextView tamilword, synonyms, antonyms;
    String tamil_des1;
    String synonyms1;
    String antonyms1;
    String eng_des1;
    Cursor c = null;
    String tamil;
    String en_word1=null;
    ImageButton mic;
    String enword;
        LinearLayout aw;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchbar = findViewById(R.id.searchbar);
        search = findViewById(R.id.searchbtn);
        // historybtn = (ImageButton) findViewById(R.id.historybtn);
        //setting = (ImageButton) findViewById(R.id.setting);
        tamil_des = findViewById(R.id.example);
        eng_des = findViewById(R.id.engdes);
        tamilword = findViewById(R.id.tamilword);
        synonyms = findViewById(R.id.synonyms);
        antonyms = findViewById(R.id.antonyms);
        mic=findViewById(R.id.mic);

    aw=findViewById(R.id.aw);
    aw.setVisibility(View.GONE);

        mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent
                        = new Intent(ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(EXTRA_LANGUAGE_MODEL,
                        LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(EXTRA_LANGUAGE,
                        Locale.getDefault());
                intent.putExtra(EXTRA_PROMPT, "Speak to text");
                try {
                    startActivityIfNeeded(intent, 200);
                } catch (Exception e) {
                    makeText(MainActivity.this, "" + e.getMessage(),
                            LENGTH_SHORT)
                            .show();
                }
            }
        });



        mydbhelp = new databasehelper(this);
        final String[] en_word = {searchbar.getText().toString()};

       // if (en_word[0].length() == 0) {

         //   Toast.makeText(getApplicationContext(), " Enter some word", Toast.LENGTH_SHORT).show();
        //    getsome();
           // return;
       // }

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

/*                Bundle bundle = getIntent().getExtras();
                String enWord= bundle.getString("enWord");
                if(enWord!=null){

                    final String en_word =enWord.toString().toLowerCase();
                    getmeaning(en_word);
                }

              /*      Bundle extras = getIntent().getExtras();
                    String en_word1 = extras.getString("en_word");
                    Toast.makeText(getApplicationContext(),"oooooooooo",Toast.LENGTH_SHORT).show();*/


                String en_word = searchbar.getText().toString().toLowerCase();

                eng_des.setText("No words");
                tamil_des.setText("Not available");
                tamilword.setText("no words");
                synonyms.setText("no words");
                antonyms.setText("no words");

                //mydbhelp = new databasehelper(this, en_word);
                //mydbhelp.getmean(en_word);
                getmeaning(en_word);
              //  close();
            }
        });


        savedbtn = findViewById(R.id.savedbtn);
        savedbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insert_fav();
            }
        });


        mydbhelp = new databasehelper(this);
        if (mydbhelp.checkDatabase()) {
            openDatabase();
        } else {

            loadasync loadasync = new loadasync(MainActivity.this);
            loadasync.execute();

        }

    }
    private void close() {
            View view=this.getCurrentFocus();
            if(null != view) {
                InputMethodManager manager=(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                manager.hideSoftInputFromWindow(view.getWindowToken(),0);
            }
    }
    //@RequiresApi(api = Build.VERSION_CODES.P)
    private void getmeaning(String en_word) {
        // String en_word = searchbar.getText().toString();
        Cursor c = mydbhelp.getmean(en_word);
        //mydbhelp = new databasehelper(this);

        try {
            mydbhelp.openDatabase();
        } catch (SQLException sqle) {
            throw sqle;
        }
        //  get_result();
        if (c.moveToFirst()) {
           // eng_des1 = c.getString(c.getColumnIndex("definition"));
            //  tamil_des1 = c.getString(c.getColumnIndex("Antonyms"));
            tamil = c.getString(c.getColumnIndex("tami"));
            synonyms1 = c.getString(c.getColumnIndex("Synonyms"));
            antonyms1 = c.getString(c.getColumnIndex("Antonyms"));

            if (c.getCount() == 0) {

              //  eng_des.setText("a");
                tamil_des.setText("a");
                tamilword.setText("a");
                close();
                c.close();

            } else {
               // eng_des.setText(eng_des1);
                // tamil_des.setText(tamil_des1);
                tamilword.setText(tamil);
                synonyms.setText(synonyms1);
                antonyms.setText(antonyms1);

                c.close();
            }
            insert_Value();

        }
       // return c;
    }

    protected void insert_fav() {
        String en_word = searchbar.getText().toString();
        if (en_word.length() == 0){
            Toast.makeText(getApplicationContext(),"Empty Input.. Please enter something",Toast.LENGTH_SHORT).show();
            return;
        }
        Cursor h = mydbhelp.check_data_fav(en_word);
        //  String en=h.getString(h.getColumnIndex("en_"));
        if (h.getCount() == 0) {

            mydbhelp.insertsaved(en_word);
            Toast.makeText(getApplicationContext(), en_word + " added to Saved List", Toast.LENGTH_SHORT).show();


        }
        else {
            Toast.makeText(getApplicationContext(), en_word + " already exist in Saved List", Toast.LENGTH_SHORT).show();
        }

    }




    private void insert_Value() {
        String en_word = searchbar.getText().toString();
        if (en_word.length() == 0){ return;   }

         Cursor w=mydbhelp.check_his(en_word);
        if (w.getCount() == 0 ) {
            mydbhelp.insertHistory(en_word);
        }
        else{
            return;
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.ex_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){


            case R.id.history:
            Intent intent1=new Intent(MainActivity.this, History.class);
           startActivity(intent1);
             return true;

            case R.id.saved_view:
                Intent intent3=new Intent(MainActivity.this, saved_view.class);
                startActivity(intent3);
                return true;
            case R.id.readme:
                Intent intent4=new Intent(MainActivity.this,readme.class);
                startActivity(intent4);
                return true;

            case R.id.Settings:
                Intent intent5=new Intent(MainActivity.this,Settings.class);
                startActivity(intent5);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public static void openDatabase() {
        try {
            mydbhelp.openDatabase();

            databaseOpened = true;

        } catch (SQLException e) {

            e.printStackTrace();

        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200) {
            if (resultCode == RESULT_OK && data != null) {
                ArrayList<String> result = data.getStringArrayListExtra(
                        EXTRA_RESULTS);
                antonyms.setVisibility(VISIBLE);


                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {


                    CharSequence en_word1 = result.get(0);

                    searchbar.setText(en_word1);
                    String en_word;
                    en_word = search.getText().toString();
                    getmeaning(en_word);

                }

            }
        }
    }
    @Override
    protected void onRestart() {
        super.onRestart();

    }



}





