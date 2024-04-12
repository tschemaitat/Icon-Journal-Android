package com.example.habittracker.Structs;

import com.example.habittracker.StaticClasses.Dictionary;
import com.example.habittracker.Widgets.GroupWidget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;

public class Structure {
    private static int structureKeyCount = 0;
    private int idCount = 0;
    private Integer id;
    private String name;
    private EntryWidgetParam param;
    private String type;
    private HashMap<Integer, Entry> entries;


    public Structure(String name, EntryWidgetParam param, String type){
        this.id = null;
        this.name = name;
        this.param = param;
        this.type = type;
        entries = new HashMap();
    }

    public void setNewKey(){
        if(id != null)
            throw new RuntimeException("tried to set new key of structure twice");
        this.id = structureKeyCount;
        structureKeyCount++;
    }

    public Structure(String name, EntryWidgetParam param, String type, ArrayList<EntryValueTree> entries){
        this.id = null;
        this.name = name;
        this.param = param;
        this.type = type;
        this.entries = new HashMap();
        for(EntryValueTree data: entries)
            addEntry(data);
    }
    public Structure(){
        this.id = null;
        this.name = null;
        this.param = new GroupWidget.GroupWidgetParam(null, new ArrayList<>());
        this.type = null;
        this.entries = new HashMap<>();
    }

    public void addEntry(EntryValueTree entryData){
        Entry entry = new Entry(entryData);
        entry.id = idCount;
        idCount++;
        entries.put(entry.id, entry);
    }

    public Entry getEntry(ArrayList<String> attributes){
        String name = attributes.get(0);
        for(Entry entry: entries.values()){
            if(entry.entryValueTree.getCachedString(0).equals(name)){
                return entry;
            }
        }
        throw new RuntimeException();
    }

    public void setData(int id, EntryValueTree data){
        Entry found = null;
        for(Entry entry: entries.values()){
            if(entry.id == id){
                found = entry;
                break;
            }
        }
        found.entryValueTree = data;
    }



    public HashMap<Integer, Entry> getEntries(){
        return entries;
    }

    public ArrayList<EntryValueTree> getData(){
        ArrayList<EntryValueTree> entryValueTrees = new ArrayList<>();
        for(Entry entry: entries.values())
            entryValueTrees.add(entry.entryValueTree);
        return entryValueTrees;
    }

    public CachedString getCachedName(){
        return new CachedString(name);
    }

    public String getType(){
        return type;
    }

    public Header getHeader(){
        Header header = new Header(param.createHeaderNode(), this);
        return header;
    }

    public ArrayList<ArrayList<String>> IdAttributes(){
        ArrayList<ArrayList<String>> names = new ArrayList<>();
        for(Entry entry: entries.values()){
            if(entry == null)
                throw new RuntimeException("entries: " + entries);
            names.add(getEntryName(entry));
        }

        return names;
    }

    private ArrayList<String> getEntryName(Entry entry) {
        ArrayList<CachedString> cachedStringList = new ArrayList<>(Collections.singleton(entry.entryValueTree.getCachedString(0)));
        ArrayList<String> result = new ArrayList<>();
        for(CachedString cachedString: cachedStringList)
            result.add(cachedString.getString());
        return result;
    }

    public EntryWidgetParam getParam(){
        return param;
    }

    public boolean isSpreadsheet() {
        if(type.equals(Dictionary.category))
            return true;
        return false;
    }

    public Entry getEntry(int entryId) {
        return entries.get(entryId);
    }

    public Integer getId() {
        return id;
    }
}
