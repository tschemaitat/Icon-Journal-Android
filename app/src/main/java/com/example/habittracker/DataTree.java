package com.example.habittracker;

import java.util.ArrayList;

public class DataTree {
    ArrayList list = new ArrayList<>();
    public DataTree(){

    }

    public void add(String string){
        list.add(string);
    }

    public void add(DataTree tree){
        list.add(tree);
    }

    public DataTree addTree(){
        DataTree tree = new DataTree();
        list.add(tree);
        return tree;
    }

    public String getString(int index){
        return ((String) list.get(index));
    }

    public DataTree getTree(int index){
        return ((DataTree) list.get(index));
    }
}
