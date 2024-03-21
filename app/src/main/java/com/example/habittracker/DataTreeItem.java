package com.example.habittracker;

import java.util.ArrayList;

public class DataTreeItem {
    ArrayList<String> path;
    public DataTreeItem(ArrayList<String> path){
        this.path = path;
    }

    public DataTreeItem(String name){
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
        if(object instanceof DataTreeItem){
            DataTreeItem item = (DataTreeItem) object;
            if(item.path.equals(path))
                return true;
        }
        return false;
    }

    public String toString(){
        return path.toString();
    }
}
