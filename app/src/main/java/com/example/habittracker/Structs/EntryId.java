package com.example.habittracker.Structs;

import java.util.Objects;

public class EntryId {
    private Integer id;

    public EntryId(Integer id) {
        this.id = id;
    }

    public Integer getInteger() {
        return id;
    }



    @Override
    public int hashCode(){
        return Integer.hashCode(id);
    }

    @Override
    public String toString(){
        return id.toString();
    }

    @Override
    public boolean equals(Object object){
        if( ! (object instanceof EntryId entryId))
            return false;
        if( ! Objects.equals(entryId.id, id))
            return false;
        return true;
    }
    public void equalsThrows(Object object){
        if( ! (object instanceof EntryId entryId))
            throw new RuntimeException(object.toString());
        if(!Objects.equals(entryId.id, id))
            throw new RuntimeException("idFirst: " + entryId.id + ", id second: " + id);
    }
}
