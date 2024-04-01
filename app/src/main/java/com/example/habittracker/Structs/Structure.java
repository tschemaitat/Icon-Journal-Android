package com.example.habittracker.Structs;

import com.example.habittracker.Widgets.GroupWidget;

import java.util.ArrayList;
import java.util.Collections;

public class Structure {
    private int idCount = 0;
    private String name;
    private EntryWidgetParam param;
    private String type;
    private ArrayList<Entry> entries;


    public Structure(String name, EntryWidgetParam param, String type){
        this.name = name;
        this.param = param;
        this.type = type;
        entries = new ArrayList<>();
    }

    public Structure(String name, EntryWidgetParam param, String type, ArrayList<DataTree> entries){
        this.name = name;
        this.param = param;
        this.type = type;
        this.entries = new ArrayList<>();
        for(DataTree data: entries)
            addEntry(data);
    }
    public Structure(){
        name = null;
        param = new GroupWidget.GroupWidgetParam(null, new ArrayList<>());
        type = null;
        entries = new ArrayList<>();
    }

    public void addEntry(DataTree entryData){
        Entry entry = new Entry(entryData);
        entry.id = idCount;
        idCount++;
        entries.add(entry);
    }

    public Entry getEntry(ArrayList<String> attributes){
        String name = attributes.get(0);
        for(Entry entry: entries){
            if(entry.dataTree.getString(0).equals(name)){
                return entry;
            }
        }
        throw new RuntimeException();
    }

    public void setData(int id, DataTree data){
        Entry found = null;
        for(Entry entry: entries){
            if(entry.id == id){
                found = entry;
                break;
            }
        }
        found.dataTree = data;
    }



    public ArrayList<Entry> getEntries(){
        return entries;
    }

    public ArrayList<DataTree> getData(){
        ArrayList<DataTree> dataTrees = new ArrayList<>();
        for(Entry entry: entries)
            dataTrees.add(entry.dataTree);
        return dataTrees;
    }

    public String getName(){
        return name;
    }

    public String getType(){
        return type;
    }

    public DataTree getHeader(){
        return param.header();
    }

    public ArrayList<ArrayList<String>> IdAttributes(){
        ArrayList<ArrayList<String>> names = new ArrayList<>();
        for(Entry entry: entries){
            if(entry == null)
                throw new RuntimeException("entries: " + entries);
            names.add(getEntryName(entry));
        }

        return names;
    }

    private ArrayList<String> getEntryName(Entry entry) {
        return new ArrayList<>(Collections.singleton(entry.dataTree.getString(0)));
    }

    public EntryWidgetParam getParam(){
        return param;
    }
}
