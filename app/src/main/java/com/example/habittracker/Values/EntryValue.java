package com.example.habittracker.Values;

public class EntryValue {
    private GroupValue groupValue;
    public EntryValue(GroupValue groupValue){
        this.groupValue = groupValue;
    }

    public GroupValue getGroupValue() {
        return groupValue;
    }
}
