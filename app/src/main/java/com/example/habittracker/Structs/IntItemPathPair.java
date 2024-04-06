package com.example.habittracker.Structs;

public class IntItemPathPair {
    private int key;
    private ItemPath option;

    public int getKey() {
        return key;
    }

    public ItemPath getOption() {
        return option;
    }

    public IntItemPathPair(int key, ItemPath option){
        this.key = key;
        this.option = option;
    }
}
