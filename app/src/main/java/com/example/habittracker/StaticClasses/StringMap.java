package com.example.habittracker.StaticClasses;

import android.content.Context;

import java.util.HashMap;

public class StringMap {
    public static final int nullKeyValue = -1;
    private static int count = 0;
    private static HashMap<Integer, StringWrapper> map = new HashMap<>();
    public static void setup(Context context){

    }
    //multiple keys can point to the same string
    //the purpose is only to allow strings to be edited while staying referenced
    public static int add(String value){
        if(value == null)
            throw new RuntimeException("null value");
        int key = count;
        count++;
        if(map.get(key) != null)
            throw new RuntimeException("tried add key: " + key + ", and value: " + value + ", already had value: " + map.get(key));
        map.put(key, new StringWrapper(value));
        return key;
    }

    public static void edit(int key, String value){
        if(value == null)
            throw new RuntimeException("null value");
        if(map.get(key) == null)
            throw new RuntimeException("tried edit key: " + key + ", and value: " + value + ", which add no previous value");
        map.put(key, new StringWrapper(value));
    }

    public static void remove(int key){
        StringWrapper value = map.get(key);
        if(value == null)
            throw new RuntimeException("tried to remove: " + key + " that already has no value");
        map.remove(key);
    }

    public static String get(int key){
        StringWrapper value = map.get(key);
        if(value == null)
            throw new RuntimeException("key doesn't have value: " + key);
        return value.string;
    }



    public static class StringWrapper{
        public String string;
        public StringWrapper(String string){
            this.string = string;
        }
    }
}
