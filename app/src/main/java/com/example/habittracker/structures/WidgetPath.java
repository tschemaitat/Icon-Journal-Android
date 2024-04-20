package com.example.habittracker.structures;

import com.example.habittracker.structures.WidgetId;

import java.util.ArrayList;

public class WidgetPath {
    private ArrayList<WidgetId> WidgetIdList;
    public WidgetPath(ArrayList<WidgetId> intList){
        this.WidgetIdList = (ArrayList<WidgetId>)intList.clone();
    }


    public WidgetId get(int level){
        return WidgetIdList.get(level);
    }

    public WidgetId getLast(){
        return WidgetIdList.get(WidgetIdList.size() - 1);
    }

    public int size(){
        return WidgetIdList.size();
    }

    public String toString(){
        return WidgetIdList.toString();
    }

    public ArrayList<WidgetId> getList(){
        return (ArrayList<WidgetId>) WidgetIdList.clone();
    }







}
