package com.example.habittracker.Structs;

import java.util.ArrayList;

public class HeaderNode{
    private String name;
    private ArrayList<HeaderNode> children;
    public HeaderNode(String name){
        this.name = name;
        children = new ArrayList<>();
    }

    public void add(HeaderNode child){
        children.add(child);
    }

    public String getName(){
        return name;
    }

    public ArrayList<HeaderNode> getChildren(){
        return children;
    }

    public void traverse(ArrayList<ValueTreePath> paths, ArrayList<Integer> currentPath){
        ArrayList<Integer> pathCopy = (ArrayList<Integer>) currentPath.clone();
        paths.add(new ValueTreePath(currentPath));
        for(HeaderNode child: children){
            traverse(paths, pathCopy);
        }
    }

}