package com.example.habittracker.structurePack;

import com.example.habittracker.Structs.CachedStrings.CachedString;
import com.example.habittracker.Structs.EntryId;
import com.example.habittracker.Values.GroupValue;

import java.util.Objects;

public class EntryInStructure {
    private EntryId id;
    private GroupValue groupValue;
    private Structure structure;

    public EntryInStructure(GroupValue groupValue, EntryId id, Structure structure){
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


    public Entry getEntry() {
        return new Entry(groupValue, id);
    }

    @Override
    public boolean equals(Object object){
        if( ! (object instanceof EntryInStructure entryInStructure))
            return false;
        if( ! Objects.equals(id, entryInStructure.id))
            return false;
        if( ! Objects.equals(groupValue, entryInStructure.groupValue))
            return false;
        if( ! Objects.equals(structure, entryInStructure.structure))
            return false;
        return true;
    }

    public void equalsThrows(EntryInStructure entryInStructure) {
        groupValue.equalsThrows(entryInStructure.groupValue);
        structure.equalsThrows(entryInStructure.structure);
    }
}
