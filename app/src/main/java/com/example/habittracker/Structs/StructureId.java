package com.example.habittracker.Structs;

import java.util.Objects;

public class StructureId{
    private Integer id;

    public StructureId(Integer id) {
        this.id = id;
    }

    public Integer getId() {
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
}
