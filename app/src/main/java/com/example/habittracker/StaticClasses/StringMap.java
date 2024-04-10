package com.example.habittracker.StaticClasses;

import android.content.Context;

import com.example.habittracker.Structs.CachedString;

import java.util.HashMap;

public class StringMap {
    public static final int nullKeyValue = -1;
    private static int count = 0;
    private static HashMap<Integer, EntryCachedString> entryMap = new HashMap<>();
    private static HashMap<Integer, TemporaryCachedString> tempMap = new HashMap<>();
    private static HashMap<Integer, StaticCachedString> staticMap = new HashMap<>();

    private static HashMap<Integer, CachedString> generalMap = new HashMap<>();

    public static void setup(Context context){

    }
    //multiple keys can point to the same string
    //the purpose is only to allow strings to be edited while staying referenced
    public static CachedString addEntryValue(String value){
        count++;
        int key = count;
        checkWhileAdd(key, value);EntryCachedString cachedString = new EntryCachedString(key, value);
        entryMap.put(key, cachedString);
        generalMap.put(key, cachedString);
        return cachedString;
    }

    public static CachedString addStaticValue(String value){
        count++;
        int key = count;
        checkWhileAdd(key, value);
        StaticCachedString cachedString = new StaticCachedString(key, value);
        staticMap.put(key, cachedString);
        generalMap.put(key, cachedString);
        return cachedString;
    }

    public static void checkWhileAdd(int generatedKey, String value){
        if(value == null)
            throw new RuntimeException("null value");
        if(generalMap.get(generatedKey) != null)
            throw new RuntimeException("tried add key: " + generatedKey + ", and value: " + value + ", already had value: " + entryMap.get(generatedKey));
    }



    public static void editEntryValue(int key, String value){
        if(value == null)
            throw new RuntimeException("null value");
        if(entryMap.get(key) == null)
            throw new RuntimeException("tried edit key: " + key + ", and value: " + value + ", which add no previous value");
        EntryCachedString cachedString = new EntryCachedString(key, value);
        entryMap.put(key, cachedString);
        generalMap.put(key, cachedString);
    }

    public static void remove(CachedString cachedString){
        CachedString value = entryMap.get(cachedString.getKey());
        if(value == null)
            throw new RuntimeException("tried to remove: " + cachedString + " that already has no value");
        entryMap.remove(value.getKey());
    }

    public static CachedString get(int key){
        CachedString value = generalMap.get(key);
        if(value == null)
            throw new RuntimeException("invalid key: " + key);
        return value;
    }



    public static class StringWrapper{
        public String string;
        public StringWrapper(String string){
            this.string = string;
        }
    }

    public static class EntryCachedString extends CachedString{

        public EntryCachedString(int key, String option) {
            super(key, option);
        }
    }

    public static class TemporaryCachedString extends CachedString{

        public TemporaryCachedString(int key, String option) {
            super(key, option);
        }
    }

    public static class StaticCachedString extends CachedString{

        public StaticCachedString(int key, String option) {
            super(key, option);
        }
    }
}
