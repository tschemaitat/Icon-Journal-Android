package com.example.habittracker.structures;

import com.example.habittracker.MainActivity;
import com.example.habittracker.StaticClasses.Dictionary;
import com.example.habittracker.Structs.CachedStrings.CachedString;
import com.example.habittracker.Structs.CachedStrings.LiteralString;
import com.example.habittracker.Structs.EntryValueTree;
import com.example.habittracker.Structs.EntryWidgetParam;
import com.example.habittracker.Structs.ValueTreePath;
import com.example.habittracker.Structs.WidgetId;
import com.example.habittracker.Widgets.GroupWidget.*;
import com.example.habittracker.Widgets.GroupWidget;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class Structure {
    private static int structureKeyCount = 0;
    private int idCount = 0;
    private Integer id;
    private String name;
    private GroupWidget.GroupWidgetParam widgetParam;
    private String type;
    private HashMap<Integer, Entry> entries;
    private Header header;


    public Structure(String name, GroupWidgetParam widgetParam, String type){
        initVariables(name, widgetParam, type, null, null);
    }



    public Structure(String name, GroupWidgetParam widgetParam, String type,
                     ArrayList<Entry> entries, HashMap<Integer, ValueTreePath> oldValuePaths){
        initVariables(name, widgetParam, type, entries, oldValuePaths);
    }
    public Structure(String type){
        initVariables(null, null, type, null, null);
    }

    public void initVariables(String name, GroupWidgetParam widgetParam, String type,
                              ArrayList<Entry> entries, HashMap<Integer, ValueTreePath> oldValuePaths){
        this.id = null;
        this.name = name;
        this.widgetParam = widgetParam;
        this.type = type;
        this.entries = new HashMap();
        if(entries == null)
            this.entries = new HashMap<>();
        else{
            for(Entry data: entries)
                insertOldEntry(data);
        }

        //MainActivity.log("log param while init structure: \n" + widgetParam.hierarchyString());

        init(oldValuePaths);
    }

    public void init(HashMap<Integer, ValueTreePath> oldValuePaths){
        createHeader(oldValuePaths);
    }

    private void createHeader(HashMap<Integer, ValueTreePath> oldValuePaths) {
        this.header = new Header(widgetParam, this, oldValuePaths);
    }

    public void createId(){
        MainActivity.log("set new key for: " + getCachedName());
        if(id != null)
            throw new RuntimeException("tried to set new key of structure twice");
        this.id = structureKeyCount;
        structureKeyCount++;
    }

    private void insertOldEntry(Entry entry){
        entries.put(entry.getId(), entry);
    }

    public void addEntry(EntryValueTree entryData){
        Set<Integer> entryIdSet = entries.keySet();
        while(entryIdSet.contains(idCount))
            idCount++;
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
        return header;
    }



    public GroupWidgetParam getWidgetParam(){
        return widgetParam;
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

    public boolean equals(Object object){
        if(object instanceof Structure){
            Structure other = (Structure) object;
            if(other.getId().equals(id))
                return true;
        }
        return false;
    }

    public Entry getEntry(int entryId) {
        return entries.get(entryId);
    }

    public Integer getId() {
        if(id == null){
            MainActivity.log("structure: " + getCachedName() +", has no id");
            throw new RuntimeException();
        }

        return id;
    }

    public String toString(){
        return "<structure> " + getCachedName().getString() + ", id: " + getId();
    }

    public EntryWidgetParam getWidgetParamFromId(WidgetId widgetId) {
        return getHeader().getWidgetParamFromId(widgetId);
    }
}
