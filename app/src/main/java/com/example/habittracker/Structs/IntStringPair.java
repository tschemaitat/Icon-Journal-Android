package com.example.habittracker.Structs;

public class IntStringPair {
    private int key;
    private String option;


    public int getKeyInt(){
        return key;
    }

    public String getOption() {
        return option;
    }

    public IntStringPair(int key, String option){
        this.key = key;
        this.option = option;
    }
}
