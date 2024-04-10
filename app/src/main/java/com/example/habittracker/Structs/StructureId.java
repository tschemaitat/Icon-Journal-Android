package com.example.habittracker.Structs;

import com.example.habittracker.StaticClasses.Dictionary;

public class StructureId {
    private int id;
    public StructureId(int id){
        this.id = id;
    }

    public int getId(){
        return id;
    }

    public String toString(){
        Structure structure = Dictionary.getStructure(id);
        return "struct id: " + id + ", " + structure.getName();
    }
}
