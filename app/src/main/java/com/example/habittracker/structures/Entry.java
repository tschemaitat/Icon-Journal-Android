package com.example.habittracker.structures;

import com.example.habittracker.Structs.CachedStrings.CachedString;
import com.example.habittracker.Structs.EntryId;
import com.example.habittracker.Values.GroupValue;

public class Entry {
    private EntryId id;
    private GroupValue groupValue;
    private Structure structure;

    public Entry(GroupValue groupValue, EntryId id, Structure structure){
        this.groupValue = groupValue;
        this.id = id;
        this.structure = structure;
    }

    public CachedString getCachedName(){
        return structure.getEntryName(this);
    }

    public EntryId getId() {
        return id;
    }

    public GroupValue getGroupValue() {
        return groupValue;
    }

    public Structure getStructure() {
        return structure;
    }



}
