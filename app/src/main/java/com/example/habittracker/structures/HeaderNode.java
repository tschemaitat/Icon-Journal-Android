package com.example.habittracker.structures;

import com.example.habittracker.MainActivity;
import com.example.habittracker.StaticClasses.GLib;
import com.example.habittracker.Structs.ValueTreePath;

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

        String tabs = GLib.tabs(currentPath.size());
        //MainActivity.log(tabs+"currentPath:" + currentPath);
        ArrayList<Integer> pathCopy = (ArrayList<Integer>) currentPath.clone();
        if(currentPath.size() != 0)
            paths.add(new ValueTreePath(currentPath));
        for(int i = 0; i < children.size(); i++){

            pathCopy.add(i);
            HeaderNode child = children.get(i);
            child.traverse(paths, pathCopy);
            pathCopy.remove(pathCopy.size() - 1);
        }
    }

    public void getPathName(ArrayList<String> result, ValueTreePath indexPath, int level) {
        if(indexPath.size() > level)
            children.get(indexPath.get(level)).getPathName(result, indexPath, level + 1);
        if(level != 0)
            result.add(name);
    }

    public String hierarchyString(int numTabs){
        String tabString = GLib.tabs(numTabs);
        String result = tabString + "(" + getChildren().size() + ")" +getName() + "\n";

        for(HeaderNode node: getChildren()){
            result += node.hierarchyString(numTabs+1);
        }
        return result;
    }
}