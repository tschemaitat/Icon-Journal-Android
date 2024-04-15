package com.example.habittracker.structures;

import com.example.habittracker.MainActivity;
import com.example.habittracker.StaticClasses.Dictionary;
import com.example.habittracker.Structs.CachedStrings.CachedString;
import com.example.habittracker.Structs.CachedStrings.LiteralString;
import com.example.habittracker.Structs.EntryValueTree;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Widgets.GroupWidget;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Structure {
    private static int structureKeyCount = 0;
    private int idCount = 0;
    private Integer id;
    private String name;
    private EntryWidgetParam param;
    private String type;
    private HashMap<Integer, Entry> entries;


    public Structure(String name, EntryWidgetParam param, String type){
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
        Entry entry = new Entry(entryData, idCount, this);
        idCount++;
        entries.put(entry.getId(), entry);
    }

    public Entry getEntry(ArrayList<String> attributes){
        String name = attributes.get(0);
        for(Entry entry: entries.values()){
            if(entry.getEntryValueTree().getCachedString(0).equals(name)){
                return entry;
            }
        }
        throw new RuntimeException();
    }

    public void setData(Entry entryToEdit, EntryValueTree data){
        int entryId = entryToEdit.getId();
        Entry found = null;
        for(Entry entry: entries.values()){
            if(entry.getId() == entryId){
                found = entry;
                break;
            }
        }
        Entry newEntry = new Entry(data, entryId, this);
        entries.put(entryId, newEntry);
    }



    public ArrayList<Entry> getEntryList(){
        return new ArrayList<>(entries.values());
    }

    public ArrayList<EntryValueTree> getData(){
        ArrayList<EntryValueTree> entryValueTrees = new ArrayList<>();
        for(Entry entry: entries.values())
            entryValueTrees.add(entry.getEntryValueTree());
        return entryValueTrees;
    }

    public ArrayList<Entry> getEntries(){
        return new ArrayList<>(entries.values());
    }



    public CachedString getCachedName(){
        return new LiteralString(name);
    }

    public String getType(){
        return type;
    }

    public Header getHeader(){
        Header header = new Header(param.createHeaderNode(), this);
        return header;
    }

    public ArrayList<ArrayList<CachedString>> IdAttributes(){
        ArrayList<ArrayList<CachedString>> names = new ArrayList<>();
        for(Entry entry: entries.values()){
            if(entry == null)
                throw new RuntimeException("entries: " + entries);
            names.add(getEntryName(entry));
        }

        return names;
    }

    protected ArrayList<CachedString> getEntryName(Entry entry) {
        CachedString cachedString = entry.getEntryValueTree().getCachedString(0);
        MainActivity.log("getting entry name: " + cachedString.getString() + ", class: " + cachedString.getClass());
        MainActivity.log("entry valueTree: \n" + entry.getEntryValueTree().hierarchy());
        return new ArrayList<>(Collections.singleton(cachedString));
    }

    public EntryWidgetParam getParam(){
        return param;
    }

    public boolean isSpreadsheet() {
        if(type.equals(Dictionary.category))
            return true;
        return false;
    }

    public static boolean isSpreadsheet(String type){
        if(type.equals(Dictionary.category))
            return true;
        return false;
    }

    public Entry getEntry(int entryId) {
        return entries.get(entryId);
    }

    public Integer getId() {
        if(id == null)
            throw new RuntimeException();
        return id;
    }

    public String toString(){
        return "<structure> " + getCachedName().getString();
    }
}
