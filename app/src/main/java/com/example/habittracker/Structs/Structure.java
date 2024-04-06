package com.example.habittracker.Structs;

import com.example.habittracker.StaticClasses.Dictionary;
import com.example.habittracker.Widgets.GroupWidget;

import java.util.ArrayList;
import java.util.Collections;

public class Structure {
    private static int structureKeyCount = 0;
    private int idCount = 0;
    private Integer key;
    private String name;
    private EntryWidgetParam param;
    private String type;
    private ArrayList<Entry> entries;


    public Structure(String name, EntryWidgetParam param, String type){
        this.key = null;
        this.name = name;
        this.param = param;
        this.type = type;
        entries = new ArrayList<>();
    }

    public Integer getKey(){
        return key;
    }

    public void setNewKey(){
        if(key != null)
            throw new RuntimeException("tried to set new key of structure twice");
        this.key = structureKeyCount;
        structureKeyCount++;
    }

    public Structure(String name, EntryWidgetParam param, String type, ArrayList<DataTree> entries){
        this.key = null;
        this.name = name;
        this.param = param;
        this.type = type;
        this.entries = new ArrayList<>();
        for(DataTree data: entries)
            addEntry(data);
    }
    public Structure(){
        this.key = null;
        this.name = null;
        this.param = new GroupWidget.GroupWidgetParam(null, new ArrayList<>());
        this.type = null;
        this.entries = new ArrayList<>();
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

    public boolean isSpreadsheet() {
        if(type.equals(Dictionary.category))
            return true;
        return false;
    }
}
