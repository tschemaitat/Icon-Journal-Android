package com.example.habittracker;

import java.util.ArrayList;

public class DataTreeItem {
    ArrayList<String> path;
    public DataTreeItem(ArrayList<String> path){
        this.path = path;
    }

    public ArrayList<String> getPath(){
        return path;
    }

    public String getName(){
        return path.get(path.size() - 1);
    }
}
