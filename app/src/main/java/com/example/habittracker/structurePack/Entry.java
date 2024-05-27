package com.example.habittracker.structurePack;


import com.example.habittracker.Algorithms.ThrowableEquals;
import com.example.habittracker.Algorithms.ThrowableEqualsWithId;
import com.example.habittracker.MainActivity;
import com.example.habittracker.Structs.EntryId;
import com.example.habittracker.Values.GroupValue;

import java.util.Objects;

public class Entry implements ThrowableEqualsWithId{
    private EntryId id;
    private GroupValue groupValue;

    public Entry(GroupValue groupValue, EntryId id){
        this.groupValue = groupValue;
        this.id = id;
    }

    public EntryId getId() {
        return id;
    }

    @Override
    public Integer getIntegerId(){
        return id.getInteger();
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

    public void equalsThrows(Object object){
        if( ! (object instanceof Entry entry))
            throw new RuntimeException(object.toString());
        id.equalsThrows(entry.id);
        groupValue.equalsThrows(entry.groupValue);
        if(! this.equals(object))
            throw new RuntimeException();
    }

    public String hierarchyString(){
        return "entry: " + getId() + "\n" + groupValue.hierarchy();
    }

    public String toString(){
        return "<Entry, id: " + id+", GroupValue size: "+ groupValue.getValues().size()+ ">";
    }
}
