package com.example.habittracker.Structs;

public class CachedString {
    private int key;
    private String option;


    public int getKey(){
        return key;
    }

    public String getString() {
        return option;
    }

    public CachedString(int key, String option){
        this.key = key;
        this.option = option;
    }
}
