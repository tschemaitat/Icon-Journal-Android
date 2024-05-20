package com.example.habittracker.structurePack;


import com.example.habittracker.Structs.EntryId;
import com.example.habittracker.Values.GroupValue;

import java.util.Objects;

public class Entry {
    private EntryId id;
    private GroupValue groupValue;

    public Entry(GroupValue groupValue, EntryId id){
        this.groupValue = groupValue;
        this.id = id;
    }

    public EntryId getId() {
        return id;
    }

    public GroupValue getGroupValue() {
        return groupValue;
    }

    @Override
    public boolean equals(Object object){
        if( ! (object instanceof Entry entry))
            return false;
        if( ! Objects.equals(id, entry.id))
            return false;
        if( ! Objects.equals(groupValue, entry.groupValue))
            return false;
        return true;
    }
}
