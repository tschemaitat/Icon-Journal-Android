package com.example.habittracker;

public class StructureEntry {
    public String key;
    public String[] header;
    public String[][] data;

    public StructureEntry(String key, String[] header, String[][] data) {
        this.key = key;
        this.header = header;
        this.data = data;
    }



}
