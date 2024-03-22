package com.example.habittracker.Structs;

import com.example.habittracker.DataTree;

public abstract class WidgetParam {
    public String widgetClass;
    public String hierarchyString(){
        return hierarchyString(0);
    }
    public abstract String hierarchyString(int numTabs);
    public abstract DataTree header();
}
