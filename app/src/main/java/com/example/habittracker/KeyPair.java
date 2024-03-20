package com.example.habittracker;



public class KeyPair{
    private final String key;
    private final Object value;

    public KeyPair(String Key, Object value) {
        this.key = Key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }

    public String toString(){
        return "{" + key + ", " + value + "}";
    }
}