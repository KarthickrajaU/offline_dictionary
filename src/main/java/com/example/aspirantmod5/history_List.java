package com.example.aspirantmod5;
import android.app.StatusBarManager;
import android.provider.Settings;

public class history_List{
    private final String en_word;
    private final String definition;
    private final String tamil;
   // public int get_tamil;

    public history_List(String en_word, String definition,String tamil) {
        this.en_word = en_word;
        this.definition = definition;
        this.tamil=tamil;
    }

    public String get_en_word() {
        return en_word;
    }
    public String get_tamil() {return tamil;}
    public String get_def() {
        return definition;
    }


}
