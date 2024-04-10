package com.example.habittracker.Structs;

import com.example.habittracker.StaticClasses.Dictionary;
import com.example.habittracker.Widgets.GroupWidget;

import java.util.ArrayList;
import java.util.Collections;

public class Structure {
    private static int structureKeyCount = 0;
    private int idCount = 0;
    private StructureId id;
    private String name;
    private EntryWidgetParam param;
    private String type;
    private ArrayList<Entry> entries;


    public Structure(String name, EntryWidgetParam param, String type){
        this.id = null;
        this.name = name;
        this.param = param;
        this.type = type;
        entries = new ArrayList<>();
    }

    public StructureId getId(){
        return id;
    }

    public void setNewKey(){
        if(id != null)
            throw new RuntimeException("tried to set new key of structure twice");
        this.id = new StructureId(structureKeyCount);
        structureKeyCount++;
    }

    public Structure(String name, EntryWidgetParam param, String type, ArrayList<EntryValueTree> entries){
        this.id = null;
        this.name = name;
        this.param = param;
        this.type = type;
        this.entries = new ArrayList<>();
        for(EntryValueTree data: entries)
            addEntry(data);
    }
    public Structure(){
        this.id = null;
        this.name = null;
        this.param = new GroupWidget.GroupWidgetParam(null, new ArrayList<>());
        this.type = null;
        this.entries = new ArrayList<>();
    }

    public void addEntry(EntryValueTree entryData){
        Entry entry = new Entry(entryData);
        entry.id = idCount;
        idCount++;
        entries.add(entry);
    }

    public Entry getEntry(ArrayList<String> attributes){
        String name = attributes.get(0);
        for(Entry entry: entries){
            if(entry.entryValueTree.getCachedString(0).equals(name)){
                return entry;
            }
        }
        throw new RuntimeException();
    }

    public void setData(int id, EntryValueTree data){
        Entry found = null;
        for(Entry entry: entries){
            if(entry.id == id){
                found = entry;
                break;
            }
        }
        found.entryValueTree = data;
    }



    public ArrayList<Entry> getEntries(){
        return entries;
    }

    public ArrayList<EntryValueTree> getData(){
        ArrayList<EntryValueTree> entryValueTrees = new ArrayList<>();
        for(Entry entry: entries)
            entryValueTrees.add(entry.entryValueTree);
        return entryValueTrees;
    }

    public String getName(){
        return name;
    }

    public String getType(){
        return type;
    }

    public Header getHeader(){
        Header header = new Header(param.createHeaderNode());
        return header;
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
        return new ArrayList<>(Collections.singleton(entry.entryValueTree.getCachedString(0)));
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
