package com.example.aspirantmod5;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.SQLException;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Settings extends AppCompatActivity {
    databasehelper mydbhelp;
    TextView clearhis;
    Button button2;
    TextView export;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    clearhis=(TextView)findViewById(R.id.clearhis);
    export=(TextView)findViewById(R.id.export);
//button2=(Button)findViewById(R.id.button2);
        mydbhelp=new databasehelper(Settings.this);
        try{
            mydbhelp.openDatabase();
        }catch (SQLException e){
            e.printStackTrace();
        }
        export.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        mydbhelp.backup();
        Toast.makeText(getApplicationContext(),"success", Toast.LENGTH_SHORT).show();
    }
});

    clearhis.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {

clearhistory();
        }
    });
    }

    private void clearhistory() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Settings.this, R.style.MyDialogTheme);
        builder.setTitle("Are you sure?");
        builder.setMessage("All the history will be deleted");

        String positiveText = "Yes";
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //String history;
                        mydbhelp.deletehistory();
                        Toast.makeText(getApplicationContext(),"History List deleted successfully", Toast.LENGTH_SHORT).show();
                    }
                });

        String negativeText = "No";
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();

    }
}