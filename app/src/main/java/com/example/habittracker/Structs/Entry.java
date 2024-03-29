package com.example.habittracker.Structs;

import com.example.habittracker.DataTree;

public class Entry {
    public int id;
    public DataTree dataTree;
    public Entry(DataTree dataTree){
        this.dataTree = dataTree;
    }
}
