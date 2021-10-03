package com.example.aspirantmod5;


import android.annotation.SuppressLint;
import android.content.Context;
import android.database.AbstractWindowedCursor;
import android.database.Cursor;
import android.database.CursorWindow;
import android.database.SQLException;
import android.database.sqlite.SQLiteBlobTooBigException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

public class databasehelper extends SQLiteOpenHelper {

    private String DB_PATH = null;

    private static final String DB_NAME = "sample.db";

    private SQLiteDatabase mydbhelp;

    private final Context context;

    private final String table_name = "word";


    public databasehelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.context = context;
        this.DB_PATH = "/data/data/" + context.getPackageName() + "/" + "databases/";
        Log.i("DB_PATH", DB_PATH);
    }



    public void createDatabase() throws IOException {

        boolean dbExist = checkDatabase();

        if (!dbExist) {

            this.getReadableDatabase();
            try {
                copyDatabase();
            } catch (IOException e) {
                throw new Error("Error Copying Database");
            }

        }

    }


    public boolean checkDatabase() {

        SQLiteDatabase checkDB = null;

        try {

            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);


        } catch (SQLiteException e) {


        }

        if (checkDB != null) {
            checkDB.close();
        }

        return checkDB != null;


    }


    private void copyDatabase() throws IOException {

        InputStream myInput = context.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);

        byte[] buffer = new byte[512];
        int length;

        while ((length = myInput.read(buffer)) > 0) {

            myOutput.write(buffer, 0, length);

        }
            myOutput.flush();
            myOutput.close();
            myInput.close();
            close();

    }

    public void openDatabase() throws SQLException {

        String path = DB_PATH + DB_NAME;
        mydbhelp = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);

    }


    @Override
    public synchronized void close() {

        if (mydbhelp != null) {
            mydbhelp.close();
        }

        super.close();


    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        try {

            this.getReadableDatabase();
            context.deleteDatabase(DB_NAME);
            copyDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

   // @RequiresApi(api = Build.VERSION_CODES.P)
    public Cursor getmean(String en_word) {
        // SQLiteDatabase db=this.getReadableDatabase();
        Cursor c = mydbhelp.rawQuery("SELECT Synonyms,Antonyms,tami,ex FROM word WHERE en_word=lower('"+en_word+"')", null);
       AbstractWindowedCursor ac=(AbstractWindowedCursor) c;
        CursorWindow cw= null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
            cw = new CursorWindow(en_word,2000);
        }
        ac.setWindow(cw);
        return c;


    }
    @RequiresApi(api = Build.VERSION_CODES.P)
    public ArrayList<String> getSuggestions1(String s)
    {
        ArrayList<String> results=new ArrayList<String>();
        try {
            SQLiteDatabase db = getReadableDatabase();
            Cursor c = db.rawQuery("SELECT rowid _id,en_word FROM word WHERE en_word LIKE lower('" + s + "%') LIMIT 10", null);
            if (c.moveToFirst()) {
                do {
                    String value = c.getString(1);
                    results.add(value);
                }while (c.moveToNext());
            }
            c.close();
        }catch (SQLException e){
            Log.e("error", "tryin to use db" + e);
        }

        return results;
    }



    public void insertHistory(String en_word){
        // SQLiteDatabase db=this.getWritableDatabase();
        mydbhelp.execSQL("INSERT INTO history(word) VALUES  ('" +en_word+ "')");
    }



    public Cursor getHistory()
    {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c= db.rawQuery("select DISTINCT  ex,word,tami from history h join word w on h.word==w.en_word order by h.id desc",null);
        return c;

    }

    public void  deleteHistory()
    {
        mydbhelp.execSQL("DELETE  FROM history");
    }


    public void insertsaved(String en_word) {
        mydbhelp.execSQL("INSERT INTO saved(word) VALUES  ('" +en_word+ "')");
    }

    public Cursor getsaved() {
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c= db.rawQuery("select DISTINCT  ex,word,tami from saved h join word w on h.word==w.en_word order by h.id desc",null);
        return c;

    }

    public void delete_saved_word(String en_word) {
        mydbhelp.delete("saved","word= ('" +en_word+ "')",null);

    }


    public void delete_history_word(String en_word) {
        mydbhelp.delete("history","word= ('" +en_word+ "')",null);
    }

    public Cursor check_data_fav(String en_word) {
        Cursor c=mydbhelp.rawQuery("select word from saved where word= ('" +en_word+ "')",null);
               return c;
    }

    public Cursor checkDatabase_history(String en_word) {
    Cursor h=mydbhelp.rawQuery("select word from saved where word= ('" +en_word+ "')",null);
    return h;
    }

    public ArrayList<String> getmean1() {
        ArrayList<String> arrayList =new ArrayList<>();

       // Cursor cursor = mydbhelp.rawQuery("SELECT * FROM word",null);
        Cursor cursor =mydbhelp.query(table_name,null,null,null,null,null,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            cursor.getString(cursor.getColumnIndex("en_word"));
            cursor.moveToNext();
        }
        cursor.close();
        return arrayList;
    }

    public Cursor check_his(String en_word) {
        Cursor w=mydbhelp.rawQuery("select word from history where word= ('" +en_word+ "')",null);
        return w;
    }

  //  @RequiresApi(api = Build.VERSION_CODES.P)
   // @SuppressLint("NewApi")
    public Cursor getSuggestions(String s) {
        SQLiteDatabase db = this.getReadableDatabase();



        Cursor c = db.rawQuery("SELECT rowid _id,en_word FROM word WHERE en_word LIKE lower('" + s + "%') LIMIT 20", null);

        try {


                AbstractWindowedCursor ac=(AbstractWindowedCursor) c;

            CursorWindow cw= null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                cw = new CursorWindow(s,2000);
            }
            ac.setWindow(cw);



            try {

                ac.moveToNext();

            }catch (SQLiteBlobTooBigException exception){
ac.close();
cw.clear();
ac.setWindow(cw);

        }}catch (SQLiteBlobTooBigException exception){

        }
        return c;
    }

    public void deletehistory() {
        mydbhelp.execSQL("DELETE FROM history");
    }

    public void backup() {
        try{
            File sd= Environment.getStorageDirectory();
            File data=Environment.getDataDirectory();

            if(sd.canWrite()){
                String currentdbpath="/data/data/" + context.getPackageName() + "/" + "databases/";
                String backupdbpath="/download/dic.db";

                        File currentdb=new File(data,currentdbpath);
                        File backupdb=new File(sd,backupdbpath);
                        Log.d("backupdb path",""+backupdb.getAbsolutePath());
                        if (currentdb.exists()){
                            FileChannel src=new FileInputStream(currentdb).getChannel();
                            FileChannel dst=new FileOutputStream(backupdb).getChannel();
                            dst.transferFrom(src,0,src.size());
                            src.close();
                            dst.close();
                        }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}


