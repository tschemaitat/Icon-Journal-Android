package com.example.habittracker.Structs;

import com.example.habittracker.DataTree;
import com.example.habittracker.Widgets.GroupWidget;

import java.util.ArrayList;

public class Structure {
    private String name;
    private WidgetParam param;
    private String type;
    private ArrayList<DataTree> entries;


    public Structure(String name, WidgetParam param, String type){
        this.name = name;
        this.param = param;
        this.type = type;
    }
    public Structure(){
        name = "null";
        param = new GroupWidget.GroupWidgetParam(new ArrayList<>());
        type = null;
    }

    public String getName(){
        return name;
    }

    public String getType(){
        return type;
    }

    public DataTree getHeader(){
        return param.header();
    }

    public WidgetParam getParam(){
        return param;
    }
}
