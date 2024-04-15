package com.example.habittracker.structures;

import com.example.habittracker.Structs.CachedStrings.ArrayString;
import com.example.habittracker.Structs.CachedStrings.CachedString;
import com.example.habittracker.Structs.EntryValueTree;

import java.util.ArrayList;

public class Entry {
    private int id;
    private EntryValueTree entryValueTree;
    private Structure structure;

    public Entry(EntryValueTree entryValueTree, int id, Structure structure){
        this.entryValueTree = entryValueTree;
        this.id = id;
        this.structure = structure;
    }

    public ArrayList<CachedString> getCachedUniqueAttributes(){
        return structure.getEntryName(this);
    }

    public CachedString getCachedName(){
        return new ArrayString(getCachedUniqueAttributes());
    }

    public int getId() {
        return id;
    }

    public EntryValueTree getEntryValueTree() {
        return entryValueTree;
    }

    public Structure getStructure() {
        return structure;
    }



}
