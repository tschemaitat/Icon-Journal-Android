package com.example.habittracker.Structs;

import com.example.habittracker.Widgets.GroupWidget;
import com.example.habittracker.Structs.WidgetParam;

import java.util.ArrayList;

public class Structure {
    public String name;
    public WidgetParam params;
    public String type;
    public Structure(String name, WidgetParam params, String type){
        this.name = name;
        this.params = params;
        this.type = type;
    }
    public Structure(){
        name = null;
        params = new GroupWidget.GroupWidgetParam(new ArrayList<>());
        type = null;
    }
}
