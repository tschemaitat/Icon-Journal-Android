package com.example.habittracker.structurePack;

public class ListItemId{
    private Integer id;
    public ListItemId(Integer id){
        if(id == null)
            throw new RuntimeException();
        this.id = id;
    }

    public Integer getId(){
        return id;
    }

    public String toString(){
        return id.toString();
    }
}
