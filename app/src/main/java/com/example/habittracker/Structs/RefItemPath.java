package com.example.habittracker.Structs;

import com.example.habittracker.StaticClasses.StringMap;

import java.util.ArrayList;

public class RefItemPath {
    private ArrayList<Integer> path;
    public RefItemPath(ArrayList<Integer> path){
        this.path = path;
    }

    public RefItemPath(Integer key){
        path = new ArrayList<>();
        path.add(key);
    }

    public ArrayList<Integer> getPath(){
        return path;
    }

    public Integer getKey(){
        return path.get(path.size() - 1);
    }

    public String getName(){
        return StringMap.get(getKey());
    }
    @Override
    public boolean equals(Object object){
        if(object instanceof RefItemPath){
            RefItemPath item = (RefItemPath) object;
            if(item.path.equals(path))
                return true;
        }
        return false;
    }

    public String toString(){
        return path.toString();
    }
}
