package com.example.habittracker;

public class DictEntry {
    public static final String dictionary = "dictionary";
    public static final String special = "special";

    public String key;
    public String[] header;
    public String[][] data;
    public String type;



    public DictEntry(String key, String[] header, String[][] data, String type) {
        this.key = key;
        this.header = header;
        this.data = data;
        this.type = type;
    }



}
