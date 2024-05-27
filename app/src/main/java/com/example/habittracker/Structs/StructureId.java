package com.example.habittracker.Structs;

import java.util.Objects;

public class StructureId{
    private Integer id;

    public StructureId(Integer id) {
        this.id = id;
    }

    public Integer getInteger() {
        return id;
    }

    @Override
    public boolean equals(Object object){
        if( ! (object instanceof StructureId structureId))
            return false;
        if(!Objects.equals(structureId.id, id))
            return false;
        return true;
    }

    @Override
    public int hashCode(){
        return Integer.hashCode(id);
    }

    @Override
    public String toString(){
        return id.toString();
    }

    public void equalsThrows(Object object){
        if( ! (object instanceof StructureId structureId))
            throw new RuntimeException(object.toString());
        if(!Objects.equals(structureId.id, id))
            throw new RuntimeException("idFirst: " + structureId.id + ", id second: " + id);
    }
}
