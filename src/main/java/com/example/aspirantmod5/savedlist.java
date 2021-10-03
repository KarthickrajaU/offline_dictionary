package com.example.aspirantmod5;

public class savedlist{
    // private String en_word=(MainActivity.getValue().toLowerCase().toString());
    private final String en_word;
    private final String definition;
    private final String tamil;


    public savedlist(String en_word, String definition,String tamil) {
        this.en_word = en_word;
        this.definition = definition;
        this.tamil=tamil;

    }
public String get_tamil(){return tamil; }
    public String get_en_word() {
        return en_word;
    }

    public String get_def() {
        return definition;
    }

}
