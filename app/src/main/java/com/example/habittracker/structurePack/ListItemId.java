package com.example.habittracker.structurePack;

import com.example.habittracker.Algorithms.ThrowableEqualsWithId;
import com.example.habittracker.Structs.EntryId;

import java.util.Objects;

public class ListItemId implements ThrowableEqualsWithId {
    private Integer id;
    public ListItemId(Integer id){
        if(id == null)
            throw new RuntimeException();
        this.id = id;
    }

    public Integer getIntegerId(){
        return id;
    }

    public String toString(){
        return id.toString();
    }

    @Override
    public boolean equals(Object object){
        if( ! (object instanceof ListItemId listItemId))
            return false;
        if( ! Objects.equals(listItemId.id, id))
            return false;
        return true;
    }
    public void equalsThrows(Object object){
        if( ! (object instanceof ListItemId listItemId))
            throw new RuntimeException(object.toString());
        if(!Objects.equals(listItemId.id, id))
            throw new RuntimeException("idFirst: " + listItemId.id + ", id second: " + id);
    }
}
