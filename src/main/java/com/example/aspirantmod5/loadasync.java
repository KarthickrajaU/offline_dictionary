package com.example.aspirantmod5;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;
//import android.support.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import java.io.IOException;

public class loadasync extends AsyncTask<Void,Void,Boolean> {
    private final Context context;
    private AlertDialog alertDialog;
    private databasehelper mydbhelp;

    public loadasync(Context context) {

        this.context = context;
    }


    protected void onPreExecute() {
        super.onPreExecute();


        AlertDialog.Builder d = new AlertDialog.Builder(context,R.style.MyDialogTheme);
        LayoutInflater inflater = LayoutInflater.from(context);
        //@SuppressLint("StaticFieldLeak")
        View dialogView = inflater.inflate(R.layout.databasecopy, null);
        d.setTitle("Loading database....");
        d.setView(dialogView);
        alertDialog = d.create();

        alertDialog.setCancelable(false);
        alertDialog.show();
    }


    protected Boolean doInBackground(Void... params) {
        mydbhelp = new databasehelper(context);
        try {
            mydbhelp.createDatabase();
        } catch (IOException e) {
            throw new Error("database was not created");
        }
        mydbhelp.close();
        return null;
    }
    protected void onprogressupdate(Void...values){
        super.onProgressUpdate(values);
    }
    protected void OnPostExecute(Boolean result){
        super.onPostExecute(result);
    }
    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        alertDialog.dismiss();
        MainActivity.openDatabase();
    }
}

