package com.example.habittracker.structures;

import java.util.ArrayList;

public class WidgetPath {
    private ArrayList<WidgetInStructure> widgetInStructureList;
    public WidgetPath(ArrayList<WidgetInStructure> intList){
        this.widgetInStructureList = (ArrayList<WidgetInStructure>)intList.clone();
    }


    public WidgetInStructure get(int level){
        return widgetInStructureList.get(level);
    }

    public WidgetInStructure getLast(){
        return widgetInStructureList.get(widgetInStructureList.size() - 1);
    }

    public int size(){
        return widgetInStructureList.size();
    }

    public String toString(){
        return widgetInStructureList.toString();
    }

    public ArrayList<WidgetInStructure> getList(){
        return (ArrayList<WidgetInStructure>) widgetInStructureList.clone();
    }







}
