package com.example.habittracker.Structs;

import java.util.ArrayList;

public class ItemPath {
    private ArrayList<String> path;
    public ItemPath(ArrayList<String> path){
        this.path = path;
    }

    public ItemPath(String name){
        path = new ArrayList<>();
        path.add(name);
    }

    public ArrayList<String> getPath(){
        return path;
    }

    public String getName(){
        return path.get(path.size() - 1);
    }
    @Override
    public boolean equals(Object object){
        if(object instanceof ItemPath){
            ItemPath item = (ItemPath) object;
            if(item.path.equals(path))
                return true;
        }
        return false;
    }

    public String toString(){
        return path.toString();
    }
}
