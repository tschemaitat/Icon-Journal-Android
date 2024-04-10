package com.example.habittracker.Structs;

public class Entry {
    public int id;
    public EntryValueTree entryValueTree;
    public Entry(EntryValueTree entryValueTree){
        this.entryValueTree = entryValueTree;
    }
}
