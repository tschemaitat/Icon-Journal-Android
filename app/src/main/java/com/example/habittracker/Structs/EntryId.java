package com.example.habittracker.Structs;

import java.util.Objects;

public class EntryId {
    private Integer id;

    public EntryId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    @Override
    public boolean equals(Object object){
        if( ! (object instanceof EntryId entryId))
            return false;
        if(!Objects.equals(entryId.id, id))
            return false;
        return true;
    }

    @Override
    public int hashCode(){
        return Integer.hashCode(id);
    }
}
